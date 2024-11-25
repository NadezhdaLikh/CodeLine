package ru.codeline.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private final JavaMailSender javaMailSender;

    public void sendPasswordEmail(String recipientEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Your New Password");
        message.setText("Your new password is: " + password);
        javaMailSender.send(message);
    }
}
