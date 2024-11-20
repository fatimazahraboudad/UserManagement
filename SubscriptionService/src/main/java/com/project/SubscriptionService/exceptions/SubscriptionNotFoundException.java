package com.project.SubscriptionService.exceptions;


public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(String idSubscription) {
        super(ExceptionMessages.SUBSCRIPTION_NOT_FOUND.getMessage(idSubscription));
    }
}
