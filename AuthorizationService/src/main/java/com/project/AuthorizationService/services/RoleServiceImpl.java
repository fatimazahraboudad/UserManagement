package com.project.AuthorizationService.services;


import com.project.AuthorizationService.dtos.RoleDto;
import com.project.AuthorizationService.entities.Role;
import com.project.AuthorizationService.exceptions.RoleAlreadyExist;
import com.project.AuthorizationService.exceptions.RoleNotFoundException;
import com.project.AuthorizationService.mappers.RoleMapper;
import com.project.AuthorizationService.repositories.RoleRepository;
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

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        if(roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RoleAlreadyExist(roleDto.getName());
        }
        Role role= RoleMapper.mapper.toEntity(roleDto);
        role.setIdRole(UUID.randomUUID().toString());
        return RoleMapper.mapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleDto getRoleById(String idRole) {
        return RoleMapper.mapper.toDto(helper(idRole));
    }

    @Override
    public List<RoleDto> getAllRole() {
        return RoleMapper.mapper.toDtos(roleRepository.findAll());
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Role role = helper(roleDto.getIdRole());
        role.setName(roleDto.getName());
        return RoleMapper.mapper.toDto(roleRepository.save(role));
    }

    @Override
    public String deleteRole(String idRole) {
        roleRepository.deleteById(idRole);
        return "role deleted successfully";
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);
        return RoleMapper.mapper.toDto(role);
    }

    Role helper(String idRole) {
        return roleRepository.findById(idRole).orElseThrow( () -> new RoleNotFoundException(idRole));
    }
}
