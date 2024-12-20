package com.project.CompanyService.feignClient;

import com.project.CompanyService.dto.UserDto;
import com.project.CompanyService.exceptions.SomethingWrongException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class FallBackFactoryUserSubscription implements FallbackFactory<UserCompanyFeignClient> {
    @Override
    public UserCompanyFeignClient create(Throwable cause) {
        return new UserCompanyFeignClient() {
            @Override
            public ResponseEntity<UserDto> getUserById(String idUser) {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }

            @Override
            public ResponseEntity<UserDto> currentUser() {
                if (cause instanceof FeignException.ServiceUnavailable ||
                        cause instanceof TimeoutException) {
                    throw new SomethingWrongException();
                } else {
                    throw new RuntimeException(cause);
                }
            }


        };
    }
}
