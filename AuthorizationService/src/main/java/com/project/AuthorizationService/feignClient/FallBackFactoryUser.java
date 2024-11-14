package com.project.AuthorizationService.feignClient;

import com.project.AuthorizationService.dtos.UserDto;
import com.project.AuthorizationService.exceptions.SomethingWrongException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FallBackFactoryUser implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public ResponseEntity<UserDto> addAuthority(String idUser, String name) {
                if (cause instanceof FeignException.ServiceUnavailable) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }

            @Override
            public ResponseEntity<UserDto> removeAuthority(String idUser, String name) {
                if (cause instanceof FeignException.ServiceUnavailable) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }            }
        };
    }
}
