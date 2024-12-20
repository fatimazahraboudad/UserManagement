package com.project.NotificationService.services;

import com.project.NotificationService.entities.EmailNotificationEvent;
import com.project.NotificationService.entities.VerificationEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.function.Consumer;


@Service
@RequiredArgsConstructor

public class EmailConsumer {

    private final EmailTemplateService emailTemplateService;
    private final EmailSenderService emailSenderService;


    @Bean
    @Transactional
    public Consumer<EmailNotificationEvent> consumeNotif() {
        return event -> {
            try {
                // Générer le contenu de l'email en utilisant le template
                String emailContent = emailTemplateService.buildEmail(event.getName(), event.getStatus());

                // Envoyer l'email
                emailSenderService.sendEmail(
                        event.getEmail(),
                        "Subscription " + event.getStatus(),
                        emailContent
                );

                System.out.println("Email sent successfully to " + event.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to process email notification: " + e.getMessage());
            }
        };
    }

    @Bean
    public Consumer<VerificationEmail> consumeVerif() {
        return event -> {
            try {
                String emailContent = emailTemplateService.buildEmailForVerification(event.getName(), event.getToken());

                emailSenderService.sendEmail(
                        event.getEmail(),
                        "Email Verification",
                        emailContent
                );

                System.out.println("Email sent successfully to " + event.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to process email notification: " + e.getMessage());
            }
        };
    }

    @Bean
    public Consumer<VerificationEmail> consumeInvit() {
        return event -> {
            try {
//                String emailContent = emailTemplateService.buildEmailForAdminInvitation(event.getToken());

                emailSenderService.sendEmail(
                        event.getEmail(),
                        "Admin invitation",
                        emailTemplateService.buildEmailForAdminInvitation(event.getToken())
                );


                System.out.println("Email sent successfully to " + event.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to process email notification: " + e.getMessage());
            }
        };
    }

}