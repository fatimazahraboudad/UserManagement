package com.project.UserService.aspect;

import com.project.UserService.annotation.LogRequest;
import com.project.UserService.dtos.JwtAuthenticationResponse;
import com.project.UserService.entities.AuditLog;
import com.project.UserService.entities.User;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.services.AuditLogService;
import com.project.UserService.services.UserService;
import com.project.UserService.utils.GeoLocationService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final GeoLocationService geoLocationService;

    @Around("@annotation(logRequest)")
    public Object logRequest(ProceedingJoinPoint joinPoint, LogRequest logRequest) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object result = joinPoint.proceed();
        String ipAddress = request.getHeader("X-Forwarded-For");
         ipAddress = ipAddress.split(",")[0].trim();

        String address = geoLocationService.getCityFromIP(ipAddress);

        log.info("@Ip --> {}",ipAddress);
        log.info("City --> {}",address);

        String action = logRequest.action();
        User user = null;
        if (action.equals("login") || action.equals("register")) {
            user = userMapper.toEntity(Objects.requireNonNull(((ResponseEntity<JwtAuthenticationResponse>) result).getBody()).getUsers());
        } else {
            user = userMapper.toEntity(userService.getCurrentUser());

        }


        auditLogService.save(AuditLog.builder()
                .action(action)
                .id("")
                .details(Arrays.toString(joinPoint.getArgs()))
                .user(user)
                .date(LocalDateTime.now())
                .ipAddress(ipAddress)
                .address(address)
                .build());

        return result;

    }

}
