package com.project.CompanyService.exceptions;


public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String id) {
        super(ExceptionMessages.COMPANY_NOT_FOUND.getMessage(id));
    }
}
