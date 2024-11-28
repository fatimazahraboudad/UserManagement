package com.project.UserService.feignClient;

import com.project.UserService.dtos.SubscriptionDto;
import com.project.UserService.exceptions.SomethingWrongException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class FallBackFactorySubscriptionUser implements FallbackFactory<SubscriptionUserFeignClient> {
    @Override
    public SubscriptionUserFeignClient create(Throwable cause) {
        return new SubscriptionUserFeignClient() {
            @Override
            public ResponseEntity<List<SubscriptionDto>> getSubscriptionByUser(String idUser) {
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
