package com.project.SubscriptionService.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationEvent {
    private String idUser;
    private String email;
    private String name;
    private String status;
    private LocalDateTime date;
}
