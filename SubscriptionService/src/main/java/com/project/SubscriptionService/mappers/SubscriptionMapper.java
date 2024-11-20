package com.project.SubscriptionService.mappers;

import com.project.SubscriptionService.dtos.SubscriptionDto;
import com.project.SubscriptionService.entities.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionDto subscriptionDto);

    SubscriptionDto toDto(Subscription subscription);

    List<Subscription> toEntities(List<SubscriptionDto> subscriptionDtoList);

    List<SubscriptionDto> toDtos(List<Subscription> subscriptionList);
}
