package com.project.UserService.controllers;


import com.project.UserService.dtos.RoleDto;
import com.project.UserService.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addNewRole(@RequestBody RoleDto roleDto) {
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



}
