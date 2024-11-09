package com.project.UserService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    USER_NOT_FOUND("User with id %s not found."),
    USER_ALREADY_EXIST("User already exists with mail %s."),
    EMAIL_OR_PASSWORD_INVALID("email pr password invalid."),
    TOKEN_EXPIRED("Token expired, verification invalid.");

    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
