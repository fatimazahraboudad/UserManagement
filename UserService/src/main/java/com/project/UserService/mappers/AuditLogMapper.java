package com.project.UserService.mappers;

import com.project.UserService.dtos.AuditLogDto;
import com.project.UserService.entities.AuditLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLog toEntity(AuditLogDto auditLogDto);
    AuditLogDto toDto(AuditLog auditLog);
    List<AuditLogDto> toDtos(List<AuditLog> auditLogList);

}
