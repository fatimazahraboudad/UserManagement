package com.project.UserService.controllers;

import com.project.UserService.Enum.EadminInvitationStatus;
import com.project.UserService.dtos.AdminInvitationDto;
import com.project.UserService.dtos.UserDto;
import com.project.UserService.services.AdminInvitationService;
import com.project.UserService.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AdminInvitationController {

    private final AdminInvitationService adminInvitationService;
    private final UserService userService;


    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @PostMapping("/admin/invite")
    public ResponseEntity<AdminInvitationDto> inviteAdmin(@RequestBody AdminInvitationDto adminInvitationDto) {
        return new ResponseEntity<>(adminInvitationService.sendInvitation(adminInvitationDto), HttpStatus.OK);
    }

    @GetMapping("/admin/invite/verify/{token}")
    public ModelAndView verifyInvitation(@PathVariable("token") String token) {
        return adminInvitationService.verifyInvitation(token);
    }

    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("admin/invite")
    public ResponseEntity<List<AdminInvitationDto>> getAllInvitation() {
        return new ResponseEntity<>(adminInvitationService.allInvitation(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@R.ROLE_ADMIN)")
    @GetMapping("/admin/invite/{idAdminInvitation}")
    public ResponseEntity<AdminInvitationDto> getInvitationById(@PathVariable String idAdminInvitation) {
        return new ResponseEntity<>(adminInvitationService.getInvitationById(idAdminInvitation), HttpStatus.OK);
    }

    @PostMapping("/users/createInviteAccount")
    public ModelAndView createInviteAccount(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String password) {

        try {
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setPhone(phone);
            userDto.setAddress(address);
            userDto.setPassword(password);

            UserDto createdUser = userService.createInviteAccount(userDto);

            return new ModelAndView("success")
                    .addObject("message", "Account created successfully! Welcome to the Mansa platform, You are now an admin.")
                    .addObject("name", createdUser.getLastName());
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage(), e);
            return new ModelAndView("error")
                    .addObject("message", "Failed to create account. Please try again.");
        }
    }

}
