package com.project.AuthorizationService.exceptions;

public class RoleAlreadyExist extends RuntimeException{
    public RoleAlreadyExist(String name) {
        super(ExceptionMessages.ROLE_ALREADY_EXIST.getMessage(name));
    }

}
