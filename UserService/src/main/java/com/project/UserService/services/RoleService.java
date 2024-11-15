package com.project.UserService.services;

import com.project.UserService.dtos.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto addRole(RoleDto roleDto);

    RoleDto getRoleById(String idRole);

    List<RoleDto> getAllRole();

    RoleDto updateRole(RoleDto roleDto);

    String deleteRole(String idRole);

    RoleDto getRoleByName(String name);


}
