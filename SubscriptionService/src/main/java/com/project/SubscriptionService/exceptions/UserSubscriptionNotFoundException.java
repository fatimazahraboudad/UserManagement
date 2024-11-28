package com.project.SubscriptionService.exceptions;

public class UserSubscriptionNotFoundException extends RuntimeException{

    public UserSubscriptionNotFoundException(String idUser) {
        super(ExceptionMessages.USER_SUBSCRIPTION_NOT_FOUND.getMessage(idUser));
    }
}
