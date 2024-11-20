package com.project.SubscriptionService.services;

import com.project.SubscriptionService.Enum.EsubscriptionStatus;
import com.project.SubscriptionService.dtos.SubscriptionDto;
import com.project.SubscriptionService.entities.Subscription;
import com.project.SubscriptionService.exceptions.SubscriptionNotFoundException;
import com.project.SubscriptionService.mappers.SubscriptionMapper;
import com.project.SubscriptionService.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionDto add(SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDto);
        subscription.setIdSubscription(UUID.randomUUID().toString());
        subscription.setStatus(EsubscriptionStatus.PENDING);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionDto getById(String idSubscription) {
        return subscriptionMapper.toDto(helper(idSubscription));
    }

    @Override
    public List<SubscriptionDto> getAll() {
        return subscriptionMapper.toDtos(subscriptionRepository.findAll());
    }

    @Override
    public SubscriptionDto update(SubscriptionDto subscriptionDto) {
        return null;
    }

    @Override
    public String delete(String idSubscription) {
        subscriptionRepository.delete(helper(idSubscription));
        return "subscription deleted with success";
    }

    @Override
    public SubscriptionDto approveRequest(String idSubscription) {
        Subscription subscription = helper(idSubscription);
        subscription.setStatus(EsubscriptionStatus.APPROVED);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionDto rejectRequest(String idSubscription) {
        Subscription subscription = helper(idSubscription);
        subscription.setStatus(EsubscriptionStatus.REJECTED);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    public Subscription helper(String idSubscription) {
        return subscriptionRepository.findById(idSubscription).orElseThrow(() -> new SubscriptionNotFoundException(idSubscription));

    }

}
