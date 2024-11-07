package com.project.UserService.exceptions;


public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String email) {
        super(ExceptionMessages.USER_ALREADY_EXIST.getMessage(email));
    }
}
