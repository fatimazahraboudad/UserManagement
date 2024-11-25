package com.project.NotificationService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;
    Context context = new Context();

    public String buildEmail(String name, String status) {
        context.setVariable("name", name);
        context.setVariable("status", status);
        return templateEngine.process("status-email", context);
    }

    public String buildEmailForVerification(String name, String token) {
        context.setVariable("name", name);
        context.setVariable("token", token);
        return templateEngine.process("verification-email", context);
    }
    public String buildEmailForAdminInvitation( String token) {
        context.setVariable("token", token);
        return templateEngine.process("adminInvitation-email", context);
    }
}
