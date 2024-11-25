package com.project.SubscriptionService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    SUBSCRIPTION_NOT_FOUND("Subscription with id %s not found."),
    OUPS_SOMETHING_WRONG("Oups something wrong!"),
    USER_SUBSCRIPTION_NOT_FOUND("User with id %s haven't any subscription");


    private final String message;


    public String getMessage(String... args) {
        return String.format(message, (Object[]) args);
    }

}
