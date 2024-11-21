package com.project.SubscriptionService.dtos;

import com.project.SubscriptionService.Enum.EsubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubscriptionDto {


    private String idSubscription;
    private EsubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto userDto;

}
