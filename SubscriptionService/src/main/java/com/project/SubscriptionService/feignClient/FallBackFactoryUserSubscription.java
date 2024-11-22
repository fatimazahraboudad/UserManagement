package com.project.SubscriptionService.feignClient;

import com.project.SubscriptionService.dtos.UserDto;
import com.project.SubscriptionService.exceptions.SomethingWrongException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FallBackFactoryUserSubscription implements FallbackFactory<UserSubscriptionFeignClient> {
    @Override
    public UserSubscriptionFeignClient create(Throwable cause) {
        return new UserSubscriptionFeignClient() {
            @Override
            public ResponseEntity<UserDto> getUserById(String idUser) {
                throw new  SomethingWrongException();
            }

            @Override
            public ResponseEntity<UserDto> currentUser() {
                throw new  SomethingWrongException();            }

            @Override
            public ResponseEntity<UserDto> addAuthority(String idUser, String name) {
                throw new  SomethingWrongException();            }

            @Override
            public ResponseEntity<UserDto> removeAuthority(String idUser, String name) {
                throw new  SomethingWrongException();            }
        };
    }
}
