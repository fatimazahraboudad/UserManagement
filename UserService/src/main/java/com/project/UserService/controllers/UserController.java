package com.project.UserService.controllers;

import com.project.UserService.dtos.JwtAuthenticationResponse;
import com.project.UserService.dtos.SignInRequest;
import com.project.UserService.dtos.UserDto;
import com.project.UserService.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> addNewUser(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/me/{idUser}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String idUser) {
        return new ResponseEntity<>(userService.getUserById(idUser), HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> UpdateProfile(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @PutMapping("/me/status/{idUser}")
    public ResponseEntity<UserDto> UpdateStatus(@PathVariable String idUser)  {
        return new ResponseEntity<>(userService.updateStatus(idUser), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest singInd)  {
        return new ResponseEntity<>(userService.signIn(singInd), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        log.info("logout ");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }


}
