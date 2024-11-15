package com.project.UserService.mappers;

import com.project.UserService.dtos.UserDto;
import com.project.UserService.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",  uses = RoleMapper.class)
public interface UserMapper {


    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    List<User> toEntities(List<UserDto> userDtoList);

    List<UserDto> toDtos(List<User> userList);

}
