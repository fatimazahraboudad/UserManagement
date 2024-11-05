package com.project.UserService.mappers;

import com.project.UserService.dtos.UserDto;
import com.project.UserService.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    List<User> toEntities(List<UserDto> userDtoList);

    List<UserDto> toDtos(List<User> userList);

}
