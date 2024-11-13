package com.project.UserService.exceptions;

public class RolesException extends RuntimeException{
    public RolesException(String idUser,String role) {
        super(ExceptionMessages.USER_ALREADY_HAVE_ROLE.getMessage(idUser,role));
    }
}