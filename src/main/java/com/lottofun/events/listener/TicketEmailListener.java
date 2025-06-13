package com.lottofun.events.listener;

import com.lottofun.domain.Ticket;
import com.lottofun.events.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEmailListener {

    private final JavaMailSender mailSender;
    @EventListener
    public void onTicketCreated(TicketCreatedEvent event) {
        Ticket ticket = event.getTicket();
        String to = ticket.getUser().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bilet Satın Alındı!");
        message.setText("Bilet numaranız: " + ticket.getTicketNumber() +
                "\nSeçtiğiniz sayılar: " + ticket.getSelectedNumbers());

        mailSender.send(message);
    }
}
