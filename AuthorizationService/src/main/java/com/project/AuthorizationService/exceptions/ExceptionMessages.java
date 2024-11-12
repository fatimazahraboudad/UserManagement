package com.project.AuthorizationService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    ROLE_NOT_FOUND("Role with id %s not found.");

    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
