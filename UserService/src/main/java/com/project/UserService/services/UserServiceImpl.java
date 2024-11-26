package com.project.UserService.services;

import com.project.UserService.dtos.*;
import com.project.UserService.entities.AdminInvitation;
import com.project.UserService.entities.Role;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.*;
import com.project.UserService.feignClient.SubscriptionUserFeignClient;
import com.project.UserService.mappers.AdminInvitationMapper;
import com.project.UserService.mappers.RoleMapper;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.repositories.UserRepository;
import com.project.UserService.security.JwtTokenProvider;
import com.project.UserService.utils.Varibales;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

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

    //private final EmailService emailService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final SubscriptionUserFeignClient subscriptionUserFeignClient;
    private final StreamBridge streamBridge;

    @Override
    public UserDto addUser(UserDto userDto) {
        if(userRepository.findByEmailIgnoreCase(userDto.getEmail()).isPresent()){
            throw new AlreadyExistException( userDto.getEmail());
        }
        User user = userMapper.toEntity(userDto);
        user.setIdUser(UUID.randomUUID().toString());
        user.setEnabled(false);
        user.setEmailVerified(false);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        if (user.getRole() == null) {
            user.setRole(new HashSet<>());
        }

        user.getRole().add(roleMapper.toEntity(roleService.getRoleByName(Varibales.ROLE_GUEST)));

        User savedUser = userRepository.save(user);

        if (savedUser != null && savedUser.getIdUser() != null) {
            String message = verifyEmailWithSendingEmail(savedUser.getIdUser());
            System.out.println(message);
        } else {
            throw new SomethingWrongException();
        }

        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(String idUser) {
        return userMapper.toDto(helper(idUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = helper(getCurrentUser().getIdUser());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateStatus(String idUser) {
        User user = helper(idUser);
        user.setEnabled(!user.isEnabled());
        return userMapper.toDto(userRepository.save(user));
    }

    public User helper(String idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException(idUser));
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request)  {

        // Vérifier si l'utilisateur existe
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(InvalidEmailOrPasswordException::new);

        // Vérifier si l'email est vérifié
        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        // Authenticate using email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        log.info("User authenticated");
//        // Find user by email
//        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
//                .orElseThrow(InvalidEmailOrPasswordException::new);

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
                .users(userMapper.toDto(user))
                .build());
        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .users(userMapper.toDto(user))
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
        //emailService.sendVerificationEmail(helper(idUser),token);
        VerificationEmail  verificationEmail = new VerificationEmail();
        verificationEmail.setIdUser(idUser);
        verificationEmail.setEmail(helper(idUser).getEmail());
        verificationEmail.setName(helper(idUser).getLastName());
        verificationEmail.setToken(token);
        verificationEmail.setLocalDateTime(LocalDateTime.now());
        streamBridge.send("verification-topic",verificationEmail);
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
                return userMapper.toDto((User) principal);
            }
        }
        return null;

    }



    @Override
    public UserDto addAuthority(String idUser, String role) {
        User user=helper(idUser);
        Role addRole=roleMapper.toEntity(roleService.getRoleByName(role));
        if(role != null) {
            if(user.getRole()
                    .stream()
                    .anyMatch(
                        role1 -> role1.getIdRole().equals(addRole.getIdRole()))) {
                throw new RolesException(idUser, role);
            }
          user.getRole().add(addRole);
        }
        return userMapper.toDto(userRepository.save(user));
    }



    @Override
    public UserDto removeAuthority(String idUser, String role) {
        User user=helper(idUser);
        Role roleToRemove= roleMapper.toEntity(roleService.getRoleByName(role));
        if(roleToRemove != null) {
            user.getRole().removeIf(role1 -> role1.getIdRole().equals(roleToRemove.getIdRole()));
         }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<SubscriptionDto> getUserSubscriptions() {
        return subscriptionUserFeignClient.getSubscriptionByUser(getCurrentUser().getIdUser()).getBody();
    }


    @Override
    public UserDto getUserByEmail(String email) {
        User user= userRepository.findByEmail(email);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto createInviteAccount(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setIdUser(UUID.randomUUID().toString());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        if (user.getRole() == null) {
            user.setRole(new HashSet<>());
        }
        user.getRole().add(roleMapper.toEntity(roleService.getRoleByName(Varibales.ROLE_ADMIN)));
        return userMapper.toDto(userRepository.save(user));
    }

}
