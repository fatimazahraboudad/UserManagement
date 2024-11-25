package com.project.UserService.feignClient;

import com.project.UserService.dtos.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SubscriptionService", fallbackFactory = FallBackFactorySubscriptionUser.class)
public interface SubscriptionUserFeignClient {


    @GetMapping("/subscription/admin/subscriptions/user/{idUser}")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionByUser(@PathVariable String idUser);
}
