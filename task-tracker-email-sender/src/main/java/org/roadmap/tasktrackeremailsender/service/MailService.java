package org.roadmap.tasktrackeremailsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {


    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender sender;

    public void send(String to, String subject, String text) {
        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(from);
        sender.send(message);
    }


}
