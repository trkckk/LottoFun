package com.lottofun.schduler;

import com.lottofun.domain.Draw;
import com.lottofun.enums.DrawStatus;
import com.lottofun.repository.DrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final DrawRepository drawRepository;

    @Override
    public void run(String... args) {
        if (drawRepository.count() == 0) {
            Draw initialDraw = Draw.builder()
                    .drawDate(LocalDateTime.now().plusMinutes(1)) // 1 dakika sonra
                    .status(DrawStatus.DRAW_OPEN)
                    .build();
            drawRepository.save(initialDraw);
        }
    }
}
