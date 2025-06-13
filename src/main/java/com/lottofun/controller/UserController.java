package com.lottofun.controller;


import com.lottofun.domain.User;
import com.lottofun.dto.request.UserLoginRequest;
import com.lottofun.dto.request.UserRegisterRequest;


import com.lottofun.security.AuthResponse;
import com.lottofun.security.SessionTokenService;
import com.lottofun.services.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SessionTokenService sessionTokenService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        User user = userService.loginUser(request);
        String token = sessionTokenService.generateToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(token));
    }
//    @PostMapping("/login")
//    public ResponseEntity<User> loginUser(@Valid @RequestBody UserLoginRequest request) {
//        User user = userService.loginUser(request);
//        return ResponseEntity.ok(user);
//    }
}