package com.project.UserService.feignClient;

import com.project.UserService.dtos.RoleDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;

public class FallBackFactoryRole implements FallbackFactory<RoleFeignClient> {
    @Override
    public RoleFeignClient create(Throwable cause) {
        return new RoleFeignClient() {
            @Override
            public ResponseEntity<RoleDto> getRoleByName(String name) {
                return null;
            }
        };
    }
}
