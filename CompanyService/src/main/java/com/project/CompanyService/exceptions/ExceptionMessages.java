package com.project.CompanyService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    COMPANY_NOT_FOUND("Company with id %s not found."),
    COMPANY_ALREADY_EXIST("company with name %s already exist."),
    OUPS_SOMETHING_WRONG("Oups something wrong!");

    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
