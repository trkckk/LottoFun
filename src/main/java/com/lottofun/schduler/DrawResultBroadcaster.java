package com.lottofun.schduler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrawResultBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(String jsonMessage) {
        messagingTemplate.convertAndSend("/topic/draw-results", jsonMessage);
        System.out.println("📢 WebSocket ile yayınlandı: " + jsonMessage);
    }
}
