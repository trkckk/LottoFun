package com.lottofun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;

@Configuration
public class FakeMailConfig {

    private final List<SimpleMailMessage> sentMessages = new ArrayList<>();

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) {
                System.out.println("📧 [FAKE MAIL] To: " + String.join(", ", simpleMessage.getTo())
                        + ", Subject: " + simpleMessage.getSubject()
                        + ", Text: " + simpleMessage.getText());
                sentMessages.add(simpleMessage);
            }
            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                for (SimpleMailMessage msg : simpleMessages) send(msg);
            }

            @Override public MimeMessage createMimeMessage() { throw new UnsupportedOperationException(); }
            @Override public MimeMessage createMimeMessage(InputStream contentStream) {
                throw new UnsupportedOperationException();
            }
            @Override public void send(MimeMessage mimeMessage) throws org.springframework.mail.MailException {
                throw new UnsupportedOperationException();
            }
            @Override public void send(MimeMessage... mimeMessages) throws org.springframework.mail.MailException {
                throw new UnsupportedOperationException();
            }
            public void send(com.lottofun.config.MimeMessagePreparator mimeMessagePreparator) {
                throw new UnsupportedOperationException();
            }
            public void send(MimeMessagePreparator... mimeMessagePreparators) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Bean
    public List<SimpleMailMessage> sentMailList() {
        return sentMessages;
    }
}