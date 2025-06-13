package com.lottofun.config;


import com.lottofun.security.SimpleAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class SecurityConfig {

        private final SimpleAuthFilter simpleAuthFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 Console için gerekli
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/api/users/register",
                                    "/api/users/login",
                                    "/h2-console/**",
                                    "draw-websocket.html", //  WebSocket test HTML dosyasını açıyoruz
                                    "/ws/**",           // WebSocket endpoint'i de korumasız olmalı
                                    "/topic/**",        // STOMP topic erişimi
                                    "/sockjs-node/**",
                                    "/static/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(simpleAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();

        }

}