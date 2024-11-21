package com.project.SubscriptionService.feignClient;

import com.project.SubscriptionService.config.AuthFeignInterceptor;
import com.project.SubscriptionService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserService", fallbackFactory = FallBackFactoryUserSubscription.class, configuration = AuthFeignInterceptor.class)
public interface UserSubscriptionFeignClient {


    @GetMapping("/api/users/me/{idUser}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String idUser);

}
