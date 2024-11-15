package com.project.UserService.mappers;

import com.project.UserService.dtos.RoleDto;
import com.project.UserService.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    List<RoleDto> toDtos(List<Role> roleList);

    List<Role> toEntities(List<RoleDto> roleDtoList);

}
