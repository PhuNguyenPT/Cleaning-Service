package com.example.cleaning_service.notifications.consumers;

import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.notifications.services.NotificationPersistenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityNotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationPersistenceService persistenceService;

    public SecurityNotificationConsumer(
            ObjectMapper objectMapper,
            NotificationPersistenceService persistenceService) {
        this.objectMapper = objectMapper;
        this.persistenceService = persistenceService;
    }

    @KafkaListener(topics = "security-notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSecurityNotifications(String message) {
        try {
            SecurityNotification notification = objectMapper.readValue(message, SecurityNotification.class);
            log.info("Received security notification for user: {}", notification.userName());

            // Store notification for later retrieval (e.g., for users who are offline)
            persistenceService.saveNotification(notification);
            log.info("Successfully saved security notification for user: {}", notification.userName());
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize security notification", e);
        }
    }
}
