package com.lottofun.events.publisher;

import com.lottofun.domain.Ticket;
import com.lottofun.events.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishTicketCreated(Object source, Ticket ticket) {
        publisher.publishEvent(new TicketCreatedEvent(source, ticket));
    }
}
