package com.project.UserService.services;

import com.project.UserService.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendVerificationEmail(User user, String token) {
        String link = "http://localhost:8081/api/users/verify/" + token;
        String message = "Please verify your email by clicking on the following link: " + link;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Verifying your email");
        email.setText(message);

        mailSender.send(email);
    }
}
