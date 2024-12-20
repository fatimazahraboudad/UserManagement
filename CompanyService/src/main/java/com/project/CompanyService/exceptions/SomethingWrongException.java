package com.project.CompanyService.exceptions;


public class SomethingWrongException extends RuntimeException {
    public SomethingWrongException() {
        super(ExceptionMessages.OUPS_SOMETHING_WRONG.getMessage());
    }

}