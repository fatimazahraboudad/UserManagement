package com.project.UserService.feignClient;

import com.project.UserService.dtos.RoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AuthorizationService", fallbackFactory = FallBackFactoryRole.class)
public interface RoleFeignClient {

    @GetMapping("authorization/get/{name}")
    public ResponseEntity<RoleDto> getRoleByName(@PathVariable String name);

}
