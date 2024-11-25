package com.project.NotificationService.repository;

import com.project.NotificationService.entities.EmailNotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSubscriptionRepository extends JpaRepository<EmailNotificationEvent,String> {
}
