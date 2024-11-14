package com.project.AuthorizationService.controllers;

import com.project.AuthorizationService.dtos.RoleDto;
import com.project.AuthorizationService.dtos.UserDto;
import com.project.AuthorizationService.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addNewRole( @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> getAllUsers() {
        return new ResponseEntity<>(roleService.getAllRole(), HttpStatus.OK);
    }

    @GetMapping("/{idRole}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable String idRole) {
        return new ResponseEntity<>(roleService.getRoleById(idRole), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<RoleDto> updateRole( @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.updateRole(roleDto), HttpStatus.OK);
    }

    @DeleteMapping("delete/{idRole}")
    public ResponseEntity<String> deleteRole(@PathVariable String idRole) {
        return new ResponseEntity<>(roleService.deleteRole(idRole), HttpStatus.OK);
    }

    @GetMapping("get/{name}")
    public ResponseEntity<RoleDto> getRoleByName(@PathVariable String name) {
        return new ResponseEntity<>(roleService.getRoleByName(name), HttpStatus.OK);
    }

    @GetMapping("addAuthority/{idUser}/{name}")
    public ResponseEntity<UserDto> addAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(roleService.addAuthority(idUser,name), HttpStatus.OK);
    }

    @GetMapping("removeAuthority/{idUser}/{name}")
    public ResponseEntity<UserDto> removeAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(roleService.removeAuthority(idUser,name), HttpStatus.OK);
    }

}
