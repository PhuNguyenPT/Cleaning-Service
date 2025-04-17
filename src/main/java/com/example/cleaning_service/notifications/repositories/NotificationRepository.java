package com.example.cleaning_service.notifications.repositories;

import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    List<NotificationEntity> findByUserAndReadOrderByTimestampDesc(User user, boolean read);
}