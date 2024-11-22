package com.project.SubscriptionService.services;

import com.project.SubscriptionService.Enum.EsubscriptionStatus;
import com.project.SubscriptionService.dtos.SubscriptionDto;
import com.project.SubscriptionService.dtos.UserDto;
import com.project.SubscriptionService.entities.EmailNotificationEvent;
import com.project.SubscriptionService.entities.Subscription;
import com.project.SubscriptionService.exceptions.SubscriptionNotFoundException;
import com.project.SubscriptionService.feignClient.UserSubscriptionFeignClient;
import com.project.SubscriptionService.mappers.SubscriptionMapper;
import com.project.SubscriptionService.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserSubscriptionFeignClient userSubscriptionFeignClient;
    private final EmailNotificationService emailNotificationService;
    private final StreamBridge streamBridge;


    @Override
    public SubscriptionDto add(SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDto);
        subscription.setIdSubscription(UUID.randomUUID().toString());
        subscription.setStatus(EsubscriptionStatus.PENDING);

        subscription.setIdUser(userSubscriptionFeignClient.currentUser().getBody().getIdUser());

        SubscriptionDto subscriptionDto1 = subscriptionMapper.toDto(subscriptionRepository.save(subscription));
        subscriptionDto1.setUserDto(userSubscriptionFeignClient.getUserById(helper(subscriptionDto1.getIdSubscription()).getIdUser()).getBody());
        return subscriptionDto1;
    }

    @Override
    public SubscriptionDto getById(String idSubscription) {
        SubscriptionDto subscriptionDto = subscriptionMapper.toDto(helper(idSubscription));
        subscriptionDto.setUserDto(userSubscriptionFeignClient.getUserById(helper(idSubscription).getIdUser()).getBody());
        return subscriptionDto;
    }

    @Override
    public List<SubscriptionDto> getAll() {
        return subscriptionMapper.toDtos(subscriptionRepository.findAll()).stream()
                .map(
                        subscriptionDto -> getById(subscriptionDto.getIdSubscription())
                )
                .collect(Collectors.toList());
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

    @Transactional
    @Override
    public SubscriptionDto approveRequest(String idSubscription) {
        Subscription subscription = helper(idSubscription);
        subscription.setStatus(EsubscriptionStatus.APPROVED);
        SubscriptionDto subscriptionDto = subscriptionMapper.toDto(subscriptionRepository.save(subscription));
        subscriptionDto.setUserDto(userSubscriptionFeignClient.getUserById(helper(idSubscription).getIdUser()).getBody());


        userSubscriptionFeignClient.addAuthority(subscriptionDto.getUserDto().getIdUser(), "MANSA-SUBSCRIBER-GR");
        userSubscriptionFeignClient.removeAuthority(subscriptionDto.getUserDto().getIdUser(), "MANSA-GUEST-GR");
        //email


        EmailNotificationEvent event = new EmailNotificationEvent(
                subscriptionDto.getUserDto().getEmail(),
                subscriptionDto.getUserDto().getLastName(),
                "APPROVED",
                LocalDateTime.now()
        );
        streamBridge.send("notification-topic", event);

        return subscriptionDto;
    }


    @Override
    public SubscriptionDto rejectRequest(String idSubscription) {
        Subscription subscription = helper(idSubscription);
        subscription.setStatus(EsubscriptionStatus.REJECTED);
        SubscriptionDto subscriptionDto = subscriptionMapper.toDto(subscriptionRepository.save(subscription));
        subscriptionDto.setUserDto(userSubscriptionFeignClient.getUserById(helper(idSubscription).getIdUser()).getBody());

        EmailNotificationEvent event = new EmailNotificationEvent(
                subscriptionDto.getUserDto().getEmail(),
                subscriptionDto.getUserDto().getLastName(),
                "REJECTED",
                LocalDateTime.now()
        );
        streamBridge.send("notification-topic", event);

        return subscriptionDto;
    }

    public Subscription helper(String idSubscription) {
        return subscriptionRepository.findById(idSubscription).orElseThrow(() -> new SubscriptionNotFoundException(idSubscription));

    }





}
