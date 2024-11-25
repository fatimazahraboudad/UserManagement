package com.project.NotificationService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "emailSubscription")
public class EmailNotificationEvent {

    @Id
    private String id;
    private String email;
    private String name;
    private String idUser;
    private String status;
    private LocalDateTime date;
//    private String content;
}
