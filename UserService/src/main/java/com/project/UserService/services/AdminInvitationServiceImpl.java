package com.project.UserService.services;

import com.project.UserService.Enum.EadminInvitationStatus;
import com.project.UserService.dtos.AdminInvitationDto;
import com.project.UserService.dtos.UserDto;
import com.project.UserService.dtos.VerificationEmail;
import com.project.UserService.entities.AdminInvitation;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.InvitationAlreadyAcceptedException;
import com.project.UserService.exceptions.RolesException;
import com.project.UserService.exceptions.TokenExpiredException;
import com.project.UserService.mappers.AdminInvitationMapper;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.repositories.AdminInvitationRepository;
import com.project.UserService.repositories.UserRepository;
import com.project.UserService.security.JwtTokenProvider;
import com.project.UserService.utils.Varibales;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminInvitationServiceImpl implements AdminInvitationService{

    @Value("${token.signing.key}")
    private String secretKey;

    private final AdminInvitationRepository adminInvitationRepository;
    private final AdminInvitationMapper adminInvitationMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public AdminInvitationDto sendInvitation(AdminInvitationDto adminInvitationDto) {
        UserDto user = userService.getUserByEmail(adminInvitationDto.getEmail());
        if (user!= null && user.getRole().stream().anyMatch(
                role1 -> role1.getName().equals(Varibales.ROLE_ADMIN))) {
            throw new RolesException(user.getIdUser(),Varibales.ROLE_ADMIN);
        }
        AdminInvitation adminInvitation = adminInvitationMapper.toEntity(adminInvitationDto);
        adminInvitation.setIdAdminInvitation(UUID.randomUUID().toString());
        adminInvitation.setUser(userMapper.toEntity(userService.getCurrentUser()));
        adminInvitation.setStatus(EadminInvitationStatus.PENDING);
        AdminInvitation adminInvitationSaved = adminInvitationRepository.save(adminInvitation);

        generateAdminInvitation(adminInvitationSaved.getEmail(), adminInvitationSaved.getIdAdminInvitation());
        return adminInvitationMapper.toDto(adminInvitationSaved);
    }


    public void generateAdminInvitation(String email, String idAdminInvitation) {

        VerificationEmail invitationEmail = new VerificationEmail();
        invitationEmail.setEmail(email);
        invitationEmail.setIdUser(idAdminInvitation);
        invitationEmail.setToken(generateToken(idAdminInvitation));
        invitationEmail.setLocalDateTime(LocalDateTime.now());

        streamBridge.send("invitation-topic", invitationEmail);

    }

    @Override
    @Transactional
    public ModelAndView verifyInvitation(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String idAdminInvitation = claims.getSubject();
            log.info(idAdminInvitation);

            AdminInvitation adminInvitation = helper(idAdminInvitation);
            if(adminInvitation.getStatus().equals(EadminInvitationStatus.ACCEPTED)){
                return new ModelAndView("error").addObject("message", "Invitation already accepted.");
                //throw new InvitationAlreadyAcceptedException();
            }
            adminInvitation.setStatus(EadminInvitationStatus.ACCEPTED);
            updateInvitation(adminInvitationMapper.toDto(adminInvitation));

            UserDto user = userService.getUserByEmail(adminInvitation.getEmail());

            if (user != null) {
                userService.addAuthority(user.getIdUser(), Varibales.ROLE_ADMIN);

                return new ModelAndView("success")
                        .addObject("message", "Welcome to the Mansa platform, You are now an admin.")
                        .addObject("name", user.getLastName());
            } else {

                ModelAndView modelAndView = new ModelAndView("create-account");
                modelAndView.addObject("email", adminInvitation.getEmail());
                return modelAndView;
            }

        } catch (Exception e) {
            log.error("Error verifying invitation: {}", e.getMessage(), e);
            return new ModelAndView("error").addObject("message", "Invalid invitation token.");
        }
    }


    @Override
    public List<AdminInvitationDto> allInvitation() {
        return adminInvitationMapper.toDtos(adminInvitationRepository.findAll());
    }

    @Override
    public AdminInvitationDto getInvitationById(String idAdminInvitation) {
        return adminInvitationMapper.toDto(helper(idAdminInvitation));
    }

    @Override
    public AdminInvitationDto updateInvitation(AdminInvitationDto adminInvitationDto) {
        AdminInvitation adminInvitation = helper(adminInvitationDto.getIdAdminInvitation());
        adminInvitation.setStatus(adminInvitationDto.getStatus());

        return adminInvitationMapper.toDto(adminInvitationRepository.save(adminInvitation));
    }

    @Override
    public String generateToken(String idAdminInvitation) {
        return jwtTokenProvider.generateAdminInvitationToken(idAdminInvitation);
    }

    public AdminInvitation helper(String idAdminInvitation) {
        return adminInvitationRepository.findById(idAdminInvitation).orElseThrow(()-> new RuntimeException(""));
    }
}
