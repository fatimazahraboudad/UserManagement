package com.project.AuthorizationService.services;

import com.project.AuthorizationService.dtos.RoleDto;
import com.project.AuthorizationService.dtos.UserDto;

import java.util.List;

public interface RoleService {

    RoleDto addRole(RoleDto roleDto);

    RoleDto getRoleById(String idRole);

    List<RoleDto> getAllRole();

    RoleDto updateRole(RoleDto roleDto);

    String deleteRole(String idRole);

    RoleDto getRoleByName(String name);

    UserDto addAuthority(String idUser, String role);
    UserDto removeAuthority(String idUser, String role);

}
