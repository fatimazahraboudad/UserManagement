package com.project.AuthorizationService.mappers;

import com.project.AuthorizationService.dtos.RoleDto;
import com.project.AuthorizationService.entities.Role;
import org.hibernate.mapping.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);

    List<RoleDto> toDtos(List<Role> roleList);

    List<Role> toEntities(List<RoleDto> roleDtoList);

}
