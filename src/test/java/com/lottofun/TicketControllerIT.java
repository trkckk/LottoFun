package com.lottofun;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lottofun.domain.Draw;
import com.lottofun.domain.User;
import com.lottofun.dto.request.TicketRequest;
import com.lottofun.enums.DrawStatus;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import com.lottofun.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TicketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        drawRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("integrationuser");
        user.setEmail("integration@example.com");
        user.setPassword("password");
        user.setBalance(100.0);
        userRepository.save(user);
        userId = user.getId();

        Draw draw = new Draw();
        draw.setDrawDate(LocalDateTime.now().plusMinutes(5));
        draw.setStatus(DrawStatus.DRAW_OPEN);
        drawRepository.save(draw);
    }

    @Test
    @Order(1)
    void should_buy_ticket_successfully() throws Exception {
        TicketRequest request = new TicketRequest(List.of(1, 2, 3, 4, 5));

        mockMvc.perform(post("/api/tickets/buy/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNumber").exists())
                .andExpect(jsonPath("$.selectedNumbers").isArray());
    }

    @Test
    @Order(2)
    void should_return_tickets_for_user() throws Exception {
        // Bilet satın al
        TicketRequest request = new TicketRequest(List.of(1, 2, 3, 4, 5));
        mockMvc.perform(post("/api/tickets/buy/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Kullanıcının biletlerini sorgula
        mockMvc.perform(get("/api/tickets/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketNumber").exists());
    }
}
