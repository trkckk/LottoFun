package com.lottofun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Client'ın dinleyeceği adres (sunucudan mesaj almak için)
        config.enableSimpleBroker("/topic");
        // Client'ın mesaj göndereceği endpoint prefix'i (genelde kullanılmaz ama tanımlanmalı)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket bağlantı noktası
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // frontend localhost:3000 vs. için
                .withSockJS(); // SockJS fallback desteği
    }

}
