package com.project.CompanyService.exceptions;


public class CompanyAlreadyExistException extends RuntimeException{

    public CompanyAlreadyExistException(String name) {
        super(ExceptionMessages.COMPANY_ALREADY_EXIST.getMessage(name));
    }
}
