package com.project.UserService.mappers;

import com.project.UserService.dtos.AdminInvitationDto;
import com.project.UserService.entities.AdminInvitation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminInvitationMapper {

    AdminInvitation toEntity(AdminInvitationDto adminInvitationDto);

    AdminInvitationDto toDto(AdminInvitation adminInvitation);

    List<AdminInvitation> toEntities(List<AdminInvitationDto> adminInvitationDtoList);

    List<AdminInvitationDto> toDtos(List<AdminInvitation> adminInvitationList);
}
