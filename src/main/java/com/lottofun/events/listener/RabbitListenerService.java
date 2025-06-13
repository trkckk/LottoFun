package com.lottofun.events.listener;

import com.lottofun.config.RabbitConfig;
import com.lottofun.schduler.DrawResultBroadcaster;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitListenerService {

    private final DrawResultBroadcaster drawResultBroadcaster;

    @RabbitListener(queues = RabbitConfig.DRAW_CLOSED_QUEUE)
    public void handleDrawClosed(String messageJson) {
        System.out.println("📨 RabbitMQ'dan mesaj alındı:");
        System.out.println("➡️ " + messageJson);

        // websocket üzerinden yayınla
        drawResultBroadcaster.broadcast(messageJson);
    }
}
