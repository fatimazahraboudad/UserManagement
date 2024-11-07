package com.project.UserService.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {
    public InvalidEmailOrPasswordException() {
        super(ExceptionMessages.EMAIL_OR_PASSWORD_INVALID.getMessage());
    }

}
