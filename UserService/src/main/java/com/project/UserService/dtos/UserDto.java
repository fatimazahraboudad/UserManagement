package com.project.UserService.dtos;

import com.project.UserService.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String idUser;

    @NotEmpty(message = "fistName should not be empty")
    @NotNull(message = "fistName should not be null")
    private String firstName;

    @NotEmpty(message = "lastName should not be empty")
    @NotNull(message = "lastName should not be null")
    private String lastName;

    @Email(message = "email is not valid")
    private String email;

    @Size(min=10, max = 10, message = "phone number should contain 10 number")
    private String phone;

    @NotEmpty(message = "address should not be empty")
    @NotNull(message = "address should not be null")
    private String address;

    private boolean enabled;
    private boolean emailVerified;


    @ValidPassword
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
