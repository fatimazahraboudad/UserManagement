package com.project.UserService.dtos;

import com.project.UserService.Enum.EadminInvitationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AdminInvitationDto {

    private String idAdminInvitation;

    @Email(message = "email is not valid")
    @NotEmpty(message = "email should not be empty")
    @NotNull(message = "email should not be null")
    private String email;

    private EadminInvitationStatus Status;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
