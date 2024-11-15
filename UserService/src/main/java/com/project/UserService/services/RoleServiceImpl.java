package com.project.UserService.services;


import com.project.UserService.dtos.RoleDto;
import com.project.UserService.entities.Role;
import com.project.UserService.exceptions.RoleAlreadyExist;
import com.project.UserService.exceptions.RoleNotFoundException;
import com.project.UserService.mappers.RoleMapper;
import com.project.UserService.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        if(roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RoleAlreadyExist(roleDto.getName());
        }
        Role role= roleMapper.toEntity(roleDto);
        role.setIdRole(UUID.randomUUID().toString());
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleDto getRoleById(String idRole) {
        return roleMapper.toDto(helper(idRole));
    }

    @Override
    public List<RoleDto> getAllRole() {
        return roleMapper.toDtos(roleRepository.findAll());
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Role role = helper(roleDto.getIdRole());
        role.setName(roleDto.getName());
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public String deleteRole(String idRole) {
        roleRepository.deleteById(idRole);
        return "role deleted successfully";
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);
        return roleMapper.toDto(role);
    }




    Role helper(String idRole) {
        return roleRepository.findById(idRole).orElseThrow( () -> new RoleNotFoundException(idRole));
    }
}
