package com.example.cleaning_service.notifications.services;


import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.notifications.repositories.NotificationRepository;
import com.example.cleaning_service.security.entities.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;

    public NotificationPersistenceService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void saveNotification(SecurityNotification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setUser(notification.user());
        entity.setType(notification.type());
        entity.setMessage(notification.message());
        entity.setTimestamp(notification.timestamp());
        entity.setRead(false);

        notificationRepository.save(entity);
        log.debug("Persisted notification for user: {}", notification.user().getUsername());
    }

    @Transactional(readOnly = true)
    public List<NotificationEntity> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndReadOrderByTimestampDesc(user, false);
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}