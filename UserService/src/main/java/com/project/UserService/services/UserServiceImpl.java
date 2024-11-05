package com.project.UserService.services;

import com.project.UserService.dtos.UserDto;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.UserNotFoundException;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.mapper.toEntity(userDto);
        user.setIdUser(UUID.randomUUID().toString());
        user.setEnabled(false);
        return UserMapper.mapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.mapper.toDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(String idUser) {
        return UserMapper.mapper.toDto(helper(idUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = helper(userDto.getIdUser());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPassword(userDto.getPassword());
        user.setUpdatedAt(LocalDateTime.now());
        return UserMapper.mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateStatus(String idUser) {
        User user = helper(idUser);
        user.setEnabled(!user.isEnabled());
        return UserMapper.mapper.toDto(userRepository.save(user));
    }

    public User helper(String idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException(idUser));
    }
}
