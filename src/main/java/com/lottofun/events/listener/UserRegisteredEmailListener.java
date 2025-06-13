package com.lottofun.events.listener;


import com.lottofun.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredEmailListener {

    private final JavaMailSender mailSender;

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        // Kullanıcıyı al
        String to = event.getUser().getEmail();

        SimpleMailMessage mesaj = new SimpleMailMessage();
        mesaj.setTo(to);
        mesaj.setSubject("Hoş geldiniz!");
        mesaj.setText("Merhaba " + event.getUser().getUsername() +
                ",\n\nLottoFun sistemine kaydınız başarıyla tamamlandı. Keyifli oyunlar dileriz!");

        // Gönder
        mailSender.send(mesaj);

        System.out.println("📧 Kayıt maili gönderildi: " + to);
    }
}
