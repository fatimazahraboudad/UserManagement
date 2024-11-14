package com.project.UserService.services;

import com.project.UserService.dtos.JwtAuthenticationResponse;
import com.project.UserService.dtos.RoleDto;
import com.project.UserService.dtos.SignInRequest;
import com.project.UserService.dtos.UserDto;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.*;
import com.project.UserService.feignClient.RoleFeignClient;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.repositories.UserRepository;
import com.project.UserService.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    @Value("${token.signing.key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;
    private final RoleFeignClient roleFeignClient;

    @Override
    public UserDto addUser(UserDto userDto) {
        if(userRepository.findByEmailIgnoreCase(userDto.getEmail()).isPresent()){
            throw new AlreadyExistException( userDto.getEmail());
        }
        User user = UserMapper.mapper.toEntity(userDto);
        user.setIdUser(UUID.randomUUID().toString());
        user.setEnabled(false);
        user.setEmailVerified(false);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));


        RoleDto roleDto = roleFeignClient.getRoleByName("MANSA-GUEST-GR").getBody();
        if(roleDto != null) user.getRole().add(roleDto.getName());

        User savedUser = userRepository.save(user);

        if (savedUser != null && savedUser.getIdUser() != null) {
            String message = verifyEmailWithSendingEmail(savedUser.getIdUser());
            System.out.println(message);
        } else {
            throw new SomethingWrongException();
        }

        return UserMapper.mapper.toDto(savedUser);
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
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
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




    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request)  {

        if(!userRepository.findByEmailIgnoreCase(request.getEmail()).get().isEmailVerified()){
            throw new EmailNotVerifiedException();
        }

        // Authenticate using email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        log.info("User authenticated");
        // Find user by email
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(InvalidEmailOrPasswordException::new);

        log.info("User found: {}", user.getEmail());
        // Check if 2FA is enabled

        // If 2FA is not enabled, proceed with generating tokens
        String jwt = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("Access token generated: {}", jwt);
        log.info("Refresh token generated: {}",refreshToken);

        log.info("User return object: {}",JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .users(UserMapper.mapper.toDto(user))
                .build());
        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .users(UserMapper.mapper.toDto(user))
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            log.info("logout service ");

            for (Cookie cookie : cookies) {
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        }
    }


    @Override
    public String verifyEmailWithSendingEmail(String idUser) {
        String token = generateToken(idUser);
        emailService.sendVerificationEmail(helper(idUser),token);
        return "please check your mail!";
    }


    @Override
    public String generateToken(String idUser) {
        return jwtTokenProvider.generateEmailVerificationToken(idUser);
    }

    @Override
    public String verifyEmail(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();

            User user = helper(userId);
            if (user.isEmailVerified()) {
                return "Email already verified";
            }
            user.setEmailVerified(true);
            userRepository.save(user);
            return "Email verified";

        } catch (ExpiredJwtException e) {
            verifyEmailWithSendingEmail(e.getClaims().getSubject());
            throw new TokenExpiredException();

        } catch (Exception e) {
            return "An error occurred during email verification.";
        }
    }

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return UserMapper.mapper.toDto((User) principal);
            }
        }
        return null;

    }



    @Override
    public UserDto addAuthority(String idUser, String role) {
        User user=helper(idUser);
        //RoleDto roleDto = roleFeignClient.getRoleByName(role).getBody();
        if(role != null) {
            if (user.getRole().contains(role)) {
                throw new RolesException(idUser, role);
            }
          user.getRole().add(role);
        }
        return UserMapper.mapper.toDto(userRepository.save(user));
    }



    @Override
    public UserDto removeAuthority(String idUser, String role) {
        User user=helper(idUser);
        //RoleDto roleDto = roleFeignClient.getRoleByName(role).getBody();
        if(role != null) {
            user.getRole().remove(role);
         }
        return UserMapper.mapper.toDto(userRepository.save(user));
    }


}
