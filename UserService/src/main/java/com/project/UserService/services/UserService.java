package com.project.UserService.services;

import com.project.UserService.dtos.JwtAuthenticationResponse;
import com.project.UserService.dtos.SignInRequest;
import com.project.UserService.dtos.UserDto;
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

}
