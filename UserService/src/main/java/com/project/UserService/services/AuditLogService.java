package com.project.UserService.services;

import com.project.UserService.dtos.AuditLogDto;
import com.project.UserService.entities.AuditLog;

import java.util.List;

public interface AuditLogService {

    void save(AuditLog auditLog);
    List<AuditLogDto> getAllLog();
}
