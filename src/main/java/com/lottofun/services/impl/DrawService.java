package com.lottofun.services.impl;


import com.lottofun.domain.Draw;
import com.lottofun.domain.Ticket;
import com.lottofun.dto.response.DrawResponseDto;
import com.lottofun.enums.DrawStatus;
import com.lottofun.enums.TicketStatus;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lottofun.dto.DrawClosedMessage;
import com.lottofun.events.publisher.RabbitEventPublisher;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawService {

    private final DrawRepository drawRepository;
    private final TicketRepository ticketRepository;
    private final RabbitEventPublisher rabbitEventPublisher;
    private final ObjectMapper objectMapper;

    public List<DrawResponseDto> getCompletedDraws(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Draw> pageResult = drawRepository.findByStatusOrderByDrawDateDesc(DrawStatus.DRAW_EXTRACTED, pageable);

        return pageResult.getContent().stream()
                .map(draw -> DrawResponseDto.builder()
                        .drawId(draw.getId())
                        .drawDate(draw.getDrawDate())
                        .status(draw.getStatus())
                        .winningNumbers(draw.getWinningNumbers())
                        .build()
                ).toList();
    }

    public void closeDraw(Draw draw) {
        // 1. Kazanan numaralar kontrolü
        if (draw.getWinningNumbers() == null || draw.getWinningNumbers().isEmpty()) {
            throw new IllegalStateException("Kazanan numaralar tanımlanmamış. Çekiliş kapatılamaz.");
        }

        // 2. Çekilişi kapat
        draw.setStatus(DrawStatus.DRAW_CLOSED);
        drawRepository.save(draw);

        // 3. Kazanan numaraları al
        List<Integer> winningNumbers = draw.getWinningNumbers();

        // 4. Bu çekilişe ait tüm biletleri getir
        List<Ticket> tickets = ticketRepository.findByDraw(draw);

        for (Ticket ticket : tickets) {
            long matchCount = ticket.getSelectedNumbers().stream()
                    .filter(winningNumbers::contains)
                    .count();

            ticket.setMatchCount((int) matchCount);

            if (matchCount >= 3) {
                ticket.setStatus(TicketStatus.WON);
                ticket.setPrize(matchCount * 50.0); // örnek ödül hesabı
            } else {
                ticket.setStatus(TicketStatus.LOST);
                ticket.setPrize(0.0);
            }

            ticketRepository.save(ticket);
        }

        // 5. RabbitMQ mesajı gönder
        try {
            DrawClosedMessage message = new DrawClosedMessage(draw.getId(), LocalDateTime.now(),draw.getWinningNumbers());
            String json = objectMapper.writeValueAsString(message);
            rabbitEventPublisher.sendDrawClosedMessage(json);
        } catch (Exception e) {
            System.err.println(" RabbitMQ mesajı gönderilemedi: " + e.getMessage());
        }

        // 6. Log
        System.out.println("✅ Çekiliş kapatıldı → ID: " + draw.getId() +
                ", Kazanan numaralar: " + winningNumbers);
    }
}