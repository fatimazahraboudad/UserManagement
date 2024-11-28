package com.project.SubscriptionService.feignClient;

import com.project.SubscriptionService.dtos.UserDto;
import com.project.SubscriptionService.exceptions.SomethingWrongException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class FallBackFactoryUserSubscription implements FallbackFactory<UserSubscriptionFeignClient> {
    @Override
    public UserSubscriptionFeignClient create(Throwable cause) {
        return new UserSubscriptionFeignClient() {
            @Override
            public ResponseEntity<UserDto> getUserById(String idUser) {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }

            @Override
            public ResponseEntity<UserDto> currentUser() {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }           }

            @Override
            public ResponseEntity<UserDto> addAuthority(String idUser, String name) {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }

            @Override
            public ResponseEntity<UserDto> removeAuthority(String idUser, String name) {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }
        };
    }
}
