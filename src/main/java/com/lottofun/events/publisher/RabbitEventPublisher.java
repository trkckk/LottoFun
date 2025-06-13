package com.lottofun.events.publisher;

import com.lottofun.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisher {
    private final AmqpTemplate amqpTemplate;

    public void sendDrawClosedMessage(String messageJson) {
        amqpTemplate.convertAndSend(RabbitConfig.DRAW_CLOSED_QUEUE, messageJson);
        System.out.println("📤 RabbitMQ mesajı gönderildi → " + messageJson);
    }
}
