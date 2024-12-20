package com.project.UserService.services;

import com.project.UserService.dtos.AuditLogDto;
import com.project.UserService.entities.AuditLog;
import com.project.UserService.entities.Role;
import com.project.UserService.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    @Override
    public void save(AuditLog auditLog) {
        auditLog.setId(UUID.randomUUID().toString());

        auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogDto> getAllLog() {
        return auditLogRepository.findAll().stream()
                .map(auditLog ->
                        AuditLogDto.builder()
                                .date(auditLog.getDate())
                                .action(auditLog.getAction())
                                .details(auditLog.getDetails())
                                .email(auditLog.getUser().getEmail())
                                .role(auditLog.getUser().getRole().stream().map(Role::getName).toList())
                                .address(auditLog.getAddress())
                                .ipAddress(auditLog.getIpAddress())
                                .build()


                ).toList();
    }
}
