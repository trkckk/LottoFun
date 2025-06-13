package com.lottofun;

import com.lottofun.controller.advice.EmailAlreadyExistsException;
import com.lottofun.domain.User;
import com.lottofun.dto.request.UserRegisterRequest;


import com.lottofun.repository.UserRepository;

import com.lottofun.services.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_throw_exception_if_email_exists() {
        // given
        UserRegisterRequest req = new UserRegisterRequest();
        req.setUsername("test");
        req.setEmail("test@example.com");
        req.setPassword("123");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> userService.registerUser(req))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("zaten kayıtlı");
    }

    @Test
    void should_register_user_successfully() {
        // given
        UserRegisterRequest req = new UserRegisterRequest();
        req.setEmail("test@example.com");
        req.setPassword("123");
        req.setUsername("test");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("test");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encoded123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = userService.registerUser(req);

        // then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUsername()).isEqualTo("test");
        verify(userRepository).save(any(User.class));


    }
}
