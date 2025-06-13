package com.lottofun.services.impl;

import com.lottofun.domain.Draw;
import com.lottofun.domain.Ticket;
import com.lottofun.domain.User;
import com.lottofun.dto.request.TicketRequest;
import com.lottofun.dto.response.TicketResponse;
import com.lottofun.enums.DrawStatus;
import com.lottofun.enums.TicketStatus;
import com.lottofun.events.publisher.TicketEventPublisher;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import com.lottofun.repository.UserRepository;
import com.lottofun.services.BuyTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService implements BuyTicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DrawRepository drawRepository;
    @Autowired
    private TicketEventPublisher ticketEventPublisher;

    private static final double TICKET_PRICE = 10.0;

    @Transactional
    @Override
    public Ticket buyTicket(Long userId, TicketRequest request) {
        // Kullanıcı kontrolü
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        // Bakiye kontrolü
        if (user.getBalance() < TICKET_PRICE) {
            throw new IllegalArgumentException("Yetersiz bakiye");
        }

        // Aktif çekiliş bul
        Draw activeDraw = drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.DRAW_OPEN)
                .orElseThrow(() -> new IllegalStateException("Aktif çekiliş bulunamadı"));
        // Çekiliş başlamak üzere bilet satışı dursun
        if (activeDraw.getDrawDate().isBefore(LocalDateTime.now().plusSeconds(10))) {
            throw new IllegalStateException("Çekiliş başlamak üzere, bilet satışı kapalı.");
        }

        // Sayılar benzersiz mi?
        Set<Integer> numberSet = new HashSet<>(request.getSelectedNumbers());
        if (numberSet.size() != 5 || numberSet.stream().anyMatch(n -> n < 1 || n > 49)) {
            throw new IllegalArgumentException("1-49 arasında 5 farklı sayı seçilmelidir");
        }

        // Ticket oluştur
        Ticket ticket = Ticket.builder()
                .ticketNumber(UUID.randomUUID().toString())
                .selectedNumbers(request.getSelectedNumbers())
                .purchaseTime(LocalDateTime.now())
                .user(user)
                .draw(activeDraw)
                .status(TicketStatus.WAITING_FOR_DRAW)
                .build();

        // Kullanıcının bakiyesini düş
        user.setBalance(user.getBalance() - TICKET_PRICE);
        // bilet alındı eventi fırlat
        ticketEventPublisher.publishTicketCreated(this, ticket);
        return ticketRepository.save(ticket);
    }

    public List<TicketResponse> getTicketsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        return user.getTickets().stream().map(ticket -> TicketResponse.builder()
                .ticketNumber(ticket.getTicketNumber())
                .purchaseTime(ticket.getPurchaseTime())
                .selectedNumbers(ticket.getSelectedNumbers())
                .status(ticket.getStatus())
                .matchCount(ticket.getMatchCount())
                .prize(ticket.getPrize())
                .drawId(ticket.getDraw().getId())
                .drawDate(ticket.getDraw().getDrawDate())
                .winningNumbers(ticket.getDraw().getWinningNumbers())
                .build()
        ).toList();
    }

}
