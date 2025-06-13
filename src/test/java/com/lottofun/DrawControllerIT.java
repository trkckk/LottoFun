package com.lottofun;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lottofun.domain.Draw;
import com.lottofun.enums.DrawStatus;
import com.lottofun.repository.DrawRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class DrawControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        drawRepository.deleteAll();

        Draw completedDraw = new Draw();
        completedDraw.setDrawDate(LocalDateTime.now().minusDays(1));
        completedDraw.setStatus(DrawStatus.DRAW_COMPLETED);
        drawRepository.save(completedDraw);
    }

    @Test
    void should_get_draw_history() throws Exception {
        mockMvc.perform(get("/api/draws/history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
}
