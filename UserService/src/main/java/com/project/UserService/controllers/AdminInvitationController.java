package com.project.UserService.controllers;

import com.project.UserService.Enum.EadminInvitationStatus;
import com.project.UserService.dtos.AdminInvitationDto;
import com.project.UserService.services.AdminInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminInvitationController {

    private final AdminInvitationService adminInvitationService;


    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @PostMapping("/invite")
    public ResponseEntity<AdminInvitationDto> inviteAdmin(@RequestBody AdminInvitationDto adminInvitationDto) {
        return new ResponseEntity<>(adminInvitationService.sendInvitation(adminInvitationDto), HttpStatus.OK);
    }

    @PutMapping("/invite/verify/{token}")
    public ResponseEntity<AdminInvitationDto> verifyInvitation(@PathVariable("token") String token) {
        return new ResponseEntity<>(adminInvitationService.verifyInvitation(token), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/invite")
    public ResponseEntity<List<AdminInvitationDto>> getAllInvitation() {
        return new ResponseEntity<>(adminInvitationService.allInvitation(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/invite/{idAdminInvitation}")
    public ResponseEntity<AdminInvitationDto> getInvitationById(@PathVariable String idAdminInvitation) {
        return new ResponseEntity<>(adminInvitationService.getInvitationById(idAdminInvitation), HttpStatus.OK);
    }

}
