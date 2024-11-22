package com.project.NotificationService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;



    public String buildEmail(String name, String status) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("status", status);
        return templateEngine.process("status-email", context);
    }
}
