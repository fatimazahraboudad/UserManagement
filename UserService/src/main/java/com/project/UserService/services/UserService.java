package com.project.UserService.services;

import com.project.UserService.dtos.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(String idUser);

    UserDto updateUser(UserDto userDto);

    UserDto updateStatus(String idUser);

    JwtAuthenticationResponse signIn(SignInRequest request);
    void logout(HttpServletRequest request, HttpServletResponse response);

    String generateToken(String idUser);

    String verifyEmail(String token);

    String verifyEmailWithSendingEmail(String idUser);

    UserDto getCurrentUser();

    UserDto addAuthority(String idUser, String role);
    UserDto removeAuthority(String idUser, String role);

    List<SubscriptionDto> getUserSubscriptions();


}
