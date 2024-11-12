package com.project.UserService.exceptions;

public class EmailNotVerifiedException extends RuntimeException{
    public EmailNotVerifiedException() {
        super(ExceptionMessages.EMAIL_NOT_VERIFIED.getMessage());
    }
}