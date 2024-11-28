package com.project.SubscriptionService.feignClient;

import com.project.SubscriptionService.config.AuthFeignInterceptor;
import com.project.SubscriptionService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserService", fallbackFactory = FallBackFactoryUserSubscription.class)
public interface UserSubscriptionFeignClient {


    @GetMapping("/api/users/me/{idUser}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String idUser);

    @GetMapping("/api/users/me")
    public ResponseEntity<UserDto> currentUser();

    @GetMapping("/api/admin/{idUser}/{name}")
    public ResponseEntity<UserDto> addAuthority(@PathVariable String idUser,@PathVariable String name);

    @GetMapping("/api/admin/remove/{idUser}/{name}")
    public ResponseEntity<UserDto> removeAuthority(@PathVariable String idUser,@PathVariable String name);

}
