package com.project.UserService.controllers;

import com.project.UserService.annotation.LogRequest;
import com.project.UserService.dtos.*;
import com.project.UserService.services.AuditLogService;
import com.project.UserService.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @LogRequest(action = "user register")
    @PostMapping("/users/register")
    public ResponseEntity<UserDto> addNewUser(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @LogRequest(action = "get all users")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @LogRequest(action = "get user by id")
    @PreAuthorize("hasRole(@R.ROLE_GUEST) or hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_SUBSCRIBER)")
    @GetMapping("/users/me/{idUser}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String idUser) {
        return new ResponseEntity<>(userService.getUserById(idUser), HttpStatus.OK);
    }

    @LogRequest(action = "update profile")
    @PreAuthorize("hasRole(@R.ROLE_GUEST) or hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_SUBSCRIBER)")
    @PutMapping("/users/me")
    public ResponseEntity<UserDto> UpdateProfile(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @LogRequest(action = "update user status")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @PutMapping("/admin/users/{idUser}/status")
    public ResponseEntity<UserDto> UpdateStatus(@PathVariable String idUser)  {
        return new ResponseEntity<>(userService.updateStatus(idUser), HttpStatus.OK);
    }

    @LogRequest(action = "login")
    @PostMapping("/users/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest singInd) throws Exception {
        return new ResponseEntity<>(userService.signIn(singInd), HttpStatus.OK);
    }

    @LogRequest(action = "logout")
    @GetMapping("/users/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        log.info("logout ");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }


    @GetMapping("/users/verify/mail/{idUser}")
    public ResponseEntity<String> verify(@PathVariable String idUser) {
        return new ResponseEntity<>( userService.verifyEmailWithSendingEmail(idUser), HttpStatus.OK);
    }

    @GetMapping("/users/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable("token") String token) {
       return new ResponseEntity<>(userService.verifyEmail(token), HttpStatus.OK);
    }


    @PreAuthorize("hasRole(@R.ROLE_GUEST) or hasRole(@R.ROLE_ADMIN) or hasRole(@R.ROLE_SUBSCRIBER)")
    @GetMapping("/users/me")
    public ResponseEntity<UserDto> currentUser() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @LogRequest(action = "add Authority to user")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/{idUser}/{name}")
    public ResponseEntity<UserDto> addAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(userService.addAuthority(idUser,name), HttpStatus.OK);
    }

    @LogRequest(action = "remove Authority from user")
    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/remove/{idUser}/{name}")
    public ResponseEntity<UserDto> removeAuthority(@PathVariable String idUser,@PathVariable String name) {
        return new ResponseEntity<>(userService.removeAuthority(idUser,name), HttpStatus.OK);
    }

    @LogRequest(action = "get user subscription")
    @PreAuthorize("hasRole(@R.ROLE_SUBSCRIBER)")
    @GetMapping("/users/subscription")
    public ResponseEntity<List<SubscriptionDto>> getUserSubscription() {
        return new ResponseEntity<>(userService.getUserSubscriptions(), HttpStatus.OK);
    }






}
