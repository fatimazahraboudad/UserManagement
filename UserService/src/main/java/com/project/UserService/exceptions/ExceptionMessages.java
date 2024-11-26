package com.project.UserService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    USER_NOT_FOUND("User with id %s not found."),
    USER_ALREADY_EXIST("User already exists with mail %s."),
    EMAIL_OR_PASSWORD_INVALID("email pr password invalid."),
    TOKEN_EXPIRED("Token expired, verification invalid."),
    EMAIL_NOT_VERIFIED("Login failed!, please verify your mail first"),
    OUPS_SOMETHING_WRONG("Oups something wrong!"),
    USER_ALREADY_HAVE_ROLE("user with id %s already have %s role"),
    ROLE_NOT_FOUND("Role with id %s not found."),
    ROLE_NOT_FOUND_WITH_NAME("Role not found."),
    ROLE_ALREADY_EXIST("Role with name %s already exist."),
    INVITATION_ALREADY_ACCEPTED("invitation already accepted");


    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
