package com.project.SubscriptionService.services;

import com.project.SubscriptionService.dtos.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto add(SubscriptionDto subscriptionDto);
    SubscriptionDto getById(String idSubscription);
    List<SubscriptionDto> getAll();
    SubscriptionDto update(SubscriptionDto subscriptionDto);
    String delete(String idSubscription);
    SubscriptionDto approveRequest(String idSubscription);
    SubscriptionDto rejectRequest(String idSubscription);


}
