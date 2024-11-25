package com.project.SubscriptionService.repositories;

import com.project.SubscriptionService.dtos.SubscriptionDto;
import com.project.SubscriptionService.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Optional<List<Subscription>> findByIdUser(String idUser);
}
