package com.lottofun.services.impl;



import com.lottofun.controller.advice.EmailAlreadyExistsException;
import com.lottofun.controller.advice.UsernameAlreadyExistsException;
import com.lottofun.domain.User;
import com.lottofun.dto.request.UserLoginRequest;
import com.lottofun.dto.request.UserRegisterRequest;
import com.lottofun.events.UserRegisteredEvent;
import com.lottofun.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final double DEFAULT_BALANCE = 100.0;

    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Bu e-posta zaten kayıtlı.");

        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Bu kullanıcı adı alınmış.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .balance(DEFAULT_BALANCE)
                .build();
        // user registreted
        eventPublisher.publishEvent(new UserRegisteredEvent(this, user));
        return userRepository.save(user);
    }

    public User loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Şifre hatalı");
        }

        return user;
    }
}
