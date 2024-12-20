package com.project.NotificationService.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEmail {
    private String idUser;
    private String email;
    private String name;
    private String token;
    private LocalDateTime localDateTime;
}
