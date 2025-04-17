package com.example.cleaning_service.notifications.consumers;

import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.notifications.services.NotificationPersistenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityNotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private final NotificationPersistenceService persistenceService;

    public SecurityNotificationConsumer(
            SimpMessagingTemplate messagingTemplate,
            ObjectMapper objectMapper,
            NotificationPersistenceService persistenceService) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
        this.persistenceService = persistenceService;
    }

    @KafkaListener(topics = "security-notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            SecurityNotification notification = objectMapper.readValue(message, SecurityNotification.class);
            log.info("Received security notification for user: {}", notification.user().getUsername());

            // Store notification for later retrieval (e.g., for users who are offline)
            persistenceService.saveNotification(notification);

            // Forward to connected WebSocket clients
            messagingTemplate.convertAndSendToUser(
                    notification.user().getUsername(),
                    "/queue/security-updates",
                    notification
            );

            log.info("Forwarded notification to user's WebSocket queue");
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize security notification", e);
        }
    }
}
