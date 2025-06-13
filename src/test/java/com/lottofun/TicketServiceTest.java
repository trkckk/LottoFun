package com.lottofun;

import com.lottofun.domain.*;
import com.lottofun.dto.request.TicketRequest;
import com.lottofun.enums.DrawStatus;
import com.lottofun.events.publisher.TicketEventPublisher;
import com.lottofun.repository.DrawRepository;
import com.lottofun.repository.TicketRepository;
import com.lottofun.repository.UserRepository;
import com.lottofun.services.impl.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private DrawRepository drawRepository;
    @Mock
    private TicketEventPublisher ticketEventPublisher;

    @InjectMocks
    private TicketService ticketService;

    private User user;
    private Draw draw;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setBalance(100.0);

        draw = new Draw();
        draw.setId(1L);
        draw.setStatus(DrawStatus.DRAW_OPEN);
        //draw.setTicketPrice(BigDecimal.valueOf(20));
    }

    @Test
    void should_throw_exception_if_user_not_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        TicketRequest request = new TicketRequest(List.of(1, 2, 3, 4, 5));

        assertThatThrownBy(() -> ticketService.buyTicket(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kullanıcı bulunamadı");
    }



    @Test
    void should_throw_exception_if_balance_is_insufficient() {
        user.setBalance(10.0); // 20 TL yeterli değil

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.DRAW_OPEN))
                .thenReturn(Optional.of(draw));

        TicketRequest request = new TicketRequest(List.of(1, 2, 3, 4, 5));

        assertThatThrownBy(() -> ticketService.buyTicket(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kullanıcı bulunamadı");
    }


}
