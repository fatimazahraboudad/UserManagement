package com.project.AuthorizationService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    ROLE_NOT_FOUND("Role with id %s not found."),
    ROLE_NOT_FOUND_WITH_NAME("Role not found."),
    ROLE_ALREADY_EXIST("Role with name %s already exist."),
    OUPS_SOMETHING_WRONG("Oups something wrong!");
            ;

    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
