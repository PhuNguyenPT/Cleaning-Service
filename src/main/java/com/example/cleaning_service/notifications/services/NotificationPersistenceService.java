package com.example.cleaning_service.notifications.services;


import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.notifications.repositories.NotificationRepository;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;
    private final IUserService userService;

    public NotificationPersistenceService(NotificationRepository notificationRepository, IUserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveNotification(SecurityNotification notification) {
        NotificationEntity entity = new NotificationEntity();
        User user = userService.findById(notification.userId());
        entity.setUser(user);
        entity.setType(notification.type());
        entity.setMessage(notification.message());
        entity.setTimestamp(notification.timestamp());
        entity.setRead(false);

        notificationRepository.save(entity);
        log.debug("Persisted notification for user ID: {}", user.getId());
    }

    @Transactional(readOnly = true)
    public Page<NotificationEntity> getUnreadNotificationsForUser(Pageable pageable, User user) {
        return notificationRepository.findByUserAndReadOrderByTimestampDesc(pageable, user, false);
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional(readOnly = true)
    public NotificationEntity findByIdAndUser(UUID id, User user) {
        return notificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Notification with id " + id + " not found for user " + user.getId()));
    }
}