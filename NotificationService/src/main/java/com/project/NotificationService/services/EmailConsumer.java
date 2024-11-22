package com.project.NotificationService.services;

import com.project.NotificationService.entities.EmailNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailTemplateService emailTemplateService;
    private final EmailSenderService emailSenderService;


    @Bean
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
}