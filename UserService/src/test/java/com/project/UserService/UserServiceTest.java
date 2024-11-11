//package com.project.UserService;
//
//import com.project.UserService.dtos.UserDto;
//import com.project.UserService.entities.User;
//import com.project.UserService.mappers.UserMapper;
//import com.project.UserService.mappers.UserMapperImpl;
//import com.project.UserService.repositories.UserRepository;
//import com.project.UserService.services.UserService;
//import com.project.UserService.services.UserServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.verify;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserMapper userMapper;
//    @InjectMocks
//    private UserServiceImpl userService;
//    @Test
//    void getAllUsers() {
//        // Arrange
//        List<User> users = List.of(new User("7d1c7d5c-8c8e-4f7c-b6e4-5a6e8b7c8d0f", "Yassine", "Benzakour",
//                "yassine.b@gmail.com", "0678543210", "25, rue de Paris, Rabat", true,
//                "$2a$10$A6hcGhH7YX65jGnJHc6k.h/abcDyZG1QsGy9jAJ1d5Uw8deMk76Ub", false, LocalDateTime.now(), null));
//        List<UserDto> userDtos = List.of(new UserDto("7d1c7d5c-8c8e-4f7c-b6e4-5a6e8b7c8d0f", "Yassine", "Benzakour",
//                "yassine.b@gmail.com", "0678543210", "25, rue de Paris, Rabat", true,
//                "$2a$10$A6hcGhH7YX65jGnJHc6k.h/abcDyZG1QsGy9jAJ1d5Uw8deMk76Ub", false, LocalDateTime.now(), null));
//
//        // Configure les comportements des mocks
//        when(userRepository.findAll()).thenReturn(users);
//        when(userMapper.toDtos(users)).thenReturn(userDtos);
//
//        // Act
//        List<UserDto> result = userService.getAllUsers();
//
//        // Assert
//        assertEquals(userDtos, result);
//        verify(userRepository, times(1)).findAll();
//        verify(userMapper, times(1)).toDtos(users);
//    }
//}
