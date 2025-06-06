package com.example.cleaning_service.notifications.repositories;

import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    Page<NotificationEntity> findByUserAndReadOrderByTimestampDesc(Pageable pageable, User user, boolean read);

    Optional<NotificationEntity> findByIdAndUser(UUID id, User user);
}