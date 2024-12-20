package com.project.CompanyService.feignClient;

import com.project.CompanyService.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserService", fallbackFactory = FallBackFactoryUserSubscription.class)
public interface UserCompanyFeignClient {


    @GetMapping("/api/users/me/{idUser}")
    ResponseEntity<UserDto> getUserById(@PathVariable String idUser);

    @GetMapping("/api/users/me")
    ResponseEntity<UserDto> currentUser();


}
