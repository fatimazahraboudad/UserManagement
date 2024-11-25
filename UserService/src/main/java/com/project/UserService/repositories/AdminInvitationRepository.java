package com.project.UserService.repositories;

import com.project.UserService.entities.AdminInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminInvitationRepository extends JpaRepository<AdminInvitation,String> {
}
