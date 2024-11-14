package com.project.UserService.feignClient;

import com.project.UserService.dtos.RoleDto;
import com.project.UserService.exceptions.RolesException;
import com.project.UserService.exceptions.SomethingWrongException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FallBackFactoryRole implements FallbackFactory<RoleFeignClient> {
    @Override
    public RoleFeignClient create(Throwable cause) {
        return new RoleFeignClient() {
            @Override
            public ResponseEntity<RoleDto> getRoleByName(String name) {
                if (cause instanceof FeignException.ServiceUnavailable) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }
        };
    }
}
