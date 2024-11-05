package com.project.UserService.exceptions;


public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String idUser) {
        super(ExceptionMessages.USER_NOT_FOUND.getMessage(idUser));
    }
}
