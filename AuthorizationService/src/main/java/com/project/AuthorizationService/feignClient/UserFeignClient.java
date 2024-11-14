package com.project.AuthorizationService.feignClient;

import com.project.AuthorizationService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserService", fallbackFactory = FallBackFactoryUser.class)
public interface UserFeignClient {

    @GetMapping("/api/admin/{idUser}/{name}")
    public ResponseEntity<UserDto> addAuthority(@PathVariable String idUser, @PathVariable String name);

    @GetMapping("/api/admin/remove/{idUser}/{name}")
    public ResponseEntity<UserDto> removeAuthority(@PathVariable String idUser, @PathVariable String name);

}
