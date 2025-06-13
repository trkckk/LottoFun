package com.lottofun.schduler;


import com.lottofun.domain.Draw;
import com.lottofun.domain.Ticket;
import com.lottofun.domain.User;
import com.lottofun.enums.DrawStatus;
import com.lottofun.enums.TicketStatus;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import com.lottofun.services.impl.DrawService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DrawScheduler {

    private final DrawRepository drawRepository;
    private final TicketRepository ticketRepository;
    private final DrawService drawService;

    private static final int WINNING_NUMBER_COUNT = 5;
    private static final int MAX_NUMBER = 49;

    @Scheduled(fixedRate = 10000) // her 30 saniyede bir kontrol et
    @Transactional
    public void processDraws() {
        Optional<Draw> optionalDraw = drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.DRAW_OPEN);

        if (optionalDraw.isEmpty()) return;

        Draw draw = optionalDraw.get();

        if (draw.getDrawDate().isAfter(LocalDateTime.now())) {
            return; // zamanı gelmemiş
        }

        // 1. Kazanan sayıları üret
        List<Integer> winningNumbers = generateWinningNumbers();
        draw.setWinningNumbers(winningNumbers);
        draw.setStatus(DrawStatus.DRAW_EXTRACTED);

        // 2. Biletleri değerlendir
        for (Ticket ticket : draw.getTickets()) {
            int matchCount = (int) ticket.getSelectedNumbers().stream()
                    .filter(winningNumbers::contains)
                    .count();

            double prize = calculatePrize(matchCount);
            ticket.setMatchCount(matchCount);
            ticket.setPrize(prize);
            ticket.setStatus(matchCount >= 2 ? TicketStatus.WON : TicketStatus.NOT_WON);

            // Ödül kazandıysa kullanıcıya ekle
            if (prize > 0) {
                User user = ticket.getUser();
                user.setBalance(user.getBalance() + prize);
            }
        }

        // 3. Yeni çekiliş oluştur
        Draw newDraw = Draw.builder()
                .drawDate(draw.getDrawDate().plusSeconds(120))
                //.drawDate(draw.getDrawDate().plusMinutes(2)) // test için 2 dk sonra
                .status(DrawStatus.DRAW_OPEN)
                .build();

        // çekilişi kapat
        drawService.closeDraw(draw);

        // çekilişi db ye kaydet
        drawRepository.save(draw);
        drawRepository.save(newDraw);
    }

    private List<Integer> generateWinningNumbers() {
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> winners = new HashSet<>(); winners.add(1); winners.add(2); winners.add(3); winners.add(4); winners.add(5); // deneme için yazıldı kazanan numaralar
        Random random = new Random();
        while (numbers.size() < WINNING_NUMBER_COUNT) {
            numbers.add(1 + random.nextInt(MAX_NUMBER));
        }
        return new ArrayList<>(numbers); // burda winners ı verirsek istek olarak 1,2,3,4,5 atarsak kanan user a ödülün eklendiğini görmek için yazdık.
    }

    private double calculatePrize(int matchCount) {
        return switch (matchCount) {
            case 5 -> 1000.0;
            case 4 -> 500.0;
            case 3 -> 100.0;
            case 2 -> 20.0;
            default -> 0.0;
        };
    }


}
