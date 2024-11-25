package com.project.UserService.services;

import com.project.UserService.Enum.EadminInvitationStatus;
import com.project.UserService.dtos.AdminInvitationDto;
import com.project.UserService.dtos.VerificationEmail;
import com.project.UserService.entities.AdminInvitation;
import com.project.UserService.entities.User;
import com.project.UserService.exceptions.TokenExpiredException;
import com.project.UserService.mappers.AdminInvitationMapper;
import com.project.UserService.mappers.UserMapper;
import com.project.UserService.repositories.AdminInvitationRepository;
import com.project.UserService.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
        AdminInvitation adminInvitation = adminInvitationMapper.toEntity(adminInvitationDto);
        adminInvitation.setIdAdminInvitation(UUID.randomUUID().toString());
        adminInvitation.setUser(userMapper.toEntity(userService.getCurrentUser()));
        adminInvitation.setStatus(EadminInvitationStatus.PENDING);
        AdminInvitation adminInvitationSaved = adminInvitationRepository.save(adminInvitation);

        VerificationEmail invitationEmail = new VerificationEmail();
        invitationEmail.setEmail(adminInvitationSaved.getEmail());
        invitationEmail.setIdUser(adminInvitationSaved.getIdAdminInvitation());
        invitationEmail.setToken(generateToken(adminInvitationSaved.getIdAdminInvitation()));
        invitationEmail.setLocalDateTime(LocalDateTime.now());

        streamBridge.send("invitation-topic", invitationEmail);
        return adminInvitationMapper.toDto(adminInvitationSaved);
    }

    @Override
    public AdminInvitationDto verifyInvitation(String token) {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String idAdminInvitation = claims.getSubject();

        }catch (Exception e) {
        }
        return null;
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
        return null;
    }

    @Override
    public String generateToken(String idAdminInvitation) {
        return jwtTokenProvider.generateAdminInvitationToken(idAdminInvitation);
    }

    public AdminInvitation helper(String idAdminInvitation) {
        return adminInvitationRepository.findById(idAdminInvitation).orElseThrow(()-> new RuntimeException(""));
    }
}
