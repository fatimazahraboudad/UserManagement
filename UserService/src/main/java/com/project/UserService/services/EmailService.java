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
        String message = "Merci de vérifier votre email en cliquant sur le lien suivant : " + link;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Vérification de votre email");
        email.setText(message);

        mailSender.send(email);
    }
}
