package com.project.UserService.feignClient;

import com.project.UserService.dtos.SubscriptionDto;
import com.project.UserService.exceptions.SomethingWrongException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FallBackFactorySubscriptionUser implements FallbackFactory<SubscriptionUserFeignClient> {
    @Override
    public SubscriptionUserFeignClient create(Throwable cause) {
        return new SubscriptionUserFeignClient() {
            @Override
            public ResponseEntity<List<SubscriptionDto>> getSubscriptionByUser(String idUser) {
                throw new SomethingWrongException();
            }
        };
    }
}
