package com.project.UserService.services;

import com.project.UserService.dtos.JwtAuthenticationResponse;
import com.project.UserService.dtos.SignInRequest;
import com.project.UserService.dtos.UserDto;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.AlreadyExistException;
import com.project.UserService.exceptions.InvalidEmailOrPasswordException;
import com.project.UserService.exceptions.TokenExpiredException;
import com.project.UserService.exceptions.UserNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            throw new TokenExpiredException();
        } catch (Exception e) {
            return "An error occurred during email verification.";
        }
    }


}
