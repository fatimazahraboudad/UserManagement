package com.project.UserService.services;

import com.project.UserService.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(String idUser);

    UserDto updateUser(UserDto userDto);

    UserDto updateStatus(String idUser);

}
