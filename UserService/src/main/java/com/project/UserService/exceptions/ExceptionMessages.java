package com.project.UserService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    USER_NOT_FOUND("User with id %s not found.");

    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
