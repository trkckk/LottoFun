package com.lottofun.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String DRAW_CLOSED_QUEUE = "draw.closed.queue";

    @Bean
    public Queue drawClosedQueue() {
        return new Queue(DRAW_CLOSED_QUEUE, false);
    }
}
