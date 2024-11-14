package com.project.AuthorizationService.services;


import com.project.AuthorizationService.dtos.RoleDto;
import com.project.AuthorizationService.dtos.UserDto;
import com.project.AuthorizationService.entities.Role;
import com.project.AuthorizationService.exceptions.RoleAlreadyExist;
import com.project.AuthorizationService.exceptions.RoleNotFoundException;
import com.project.AuthorizationService.feignClient.UserFeignClient;
import com.project.AuthorizationService.mappers.RoleMapper;
import com.project.AuthorizationService.repositories.RoleRepository;
import feign.FeignException;
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
    private final UserFeignClient userFeignClient;

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

    @Override
    public UserDto addAuthority(String idUser, String role) {
        if(roleRepository.findByName(role).isPresent())
            return userFeignClient.addAuthority(idUser,role).getBody();
        throw new RoleNotFoundException();
    }

//    @Override
//    public UserDto addAuthority(String idUser, String role) {
//        // Vérifier si le rôle existe dans la base de données
//        if (!roleRepository.findByName(role).isPresent()) {
//            throw new RoleNotFoundException();
//        }
//
//        try {
//            // Appel du client Feign pour ajouter l'autorité à l'utilisateur
//            return userFeignClient.addAuthority(idUser, role).getBody();
//        } catch (FeignException e) {
//            // Capture l'exception et extrait le message du corps de la réponse
//            String errorMessage = "An error occurred while adding authority";
//
//            // Extraire le message d'erreur détaillé si disponible
//            if (e.responseBody().isPresent()) {
//                errorMessage = e.responseBody().get().toString();
//            }
//
//            // Afficher ou logger le message d'erreur
//            System.err.println("User Service Error: " + errorMessage);
//
//            // Lever une nouvelle exception pour notifier l'erreur
//            throw new RuntimeException("Failed to add authority: " + errorMessage, e);
//        }
//    }


    @Override
    public UserDto removeAuthority(String idUser, String role) {
        if(roleRepository.findByName(role).isPresent())
            return userFeignClient.removeAuthority(idUser,role).getBody();
        throw new RoleNotFoundException();
    }

    Role helper(String idRole) {
        return roleRepository.findById(idRole).orElseThrow( () -> new RoleNotFoundException(idRole));
    }
}
