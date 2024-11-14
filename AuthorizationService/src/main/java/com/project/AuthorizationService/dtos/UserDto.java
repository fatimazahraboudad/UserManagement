package com.project.AuthorizationService.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private boolean enabled;
    private String password;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> role;

}
