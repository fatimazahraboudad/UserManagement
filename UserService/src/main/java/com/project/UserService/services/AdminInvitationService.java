package com.project.UserService.services;

import com.project.UserService.Enum.EadminInvitationStatus;
import com.project.UserService.dtos.AdminInvitationDto;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface AdminInvitationService {

    AdminInvitationDto sendInvitation(AdminInvitationDto adminInvitationDto);
    ModelAndView verifyInvitation(String token);
    List<AdminInvitationDto> allInvitation();
    AdminInvitationDto getInvitationById(String idAdminInvitation);
    AdminInvitationDto updateInvitation(AdminInvitationDto adminInvitationDto);
    String generateToken(String idUser);

}
