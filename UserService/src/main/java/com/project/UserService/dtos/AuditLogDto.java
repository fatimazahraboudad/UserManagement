package com.project.UserService.dtos;

import com.project.UserService.entities.User;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AuditLogDto {


    private String email;
    private List<String> role;
    private LocalDateTime date;
    private String action;
    private String details;
    private String ipAddress;
    private String address;
}
