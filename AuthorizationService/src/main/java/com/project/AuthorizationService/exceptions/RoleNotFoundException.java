package com.project.AuthorizationService.exceptions;


public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String idRole) {
        super(ExceptionMessages.ROLE_NOT_FOUND.getMessage(idRole));
    }
}
