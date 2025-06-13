package com.lottofun;

import com.lottofun.domain.Draw;
import com.lottofun.domain.User;
import com.lottofun.dto.request.TicketRequest;
import com.lottofun.enums.DrawStatus;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import com.lottofun.repository.UserRepository;
import com.lottofun.services.impl.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DrawSchedulerTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Long userId;

    @BeforeEach
    void setup() {

        //her seferinde yeni eposta
        String uniqueEmail = "threadtest+" + UUID.randomUUID() + "@example.com";
        String uniqusername = "threaduser_" + UUID.randomUUID();
        // 1. Kullanıcı oluştur
        User user = new User();
        user.setUsername(uniqusername);
        user.setEmail(uniqueEmail);
        user.setPassword("123456");
        user.setBalance(10.0); // sadece 1 bilet yeterli
        user = userRepository.save(user);
        userId = user.getId();

        // 2. Aktif çekiliş oluştur
        Draw draw = Draw.builder()
                .drawDate(LocalDateTime.now().plusMinutes(1))
                .status(DrawStatus.DRAW_OPEN)
                .build();
        drawRepository.save(draw);
    }

    @Test
    void testConcurrentTicketPurchase() throws InterruptedException {
        TicketRequest request = new TicketRequest(List.of(1, 2, 3, 4, 5));
        AtomicInteger successCount = new AtomicInteger(0);

        Runnable task = () -> {
            try {
                ticketService.buyTicket(userId, request);
                successCount.incrementAndGet();
            } catch (Exception e) {
                // beklenen: biri başarısız olacak
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(1, successCount.get(), "Sadece bir bilet alınmış olmalı");
    }

    @Test
    void contextLoads() {
    }
}
