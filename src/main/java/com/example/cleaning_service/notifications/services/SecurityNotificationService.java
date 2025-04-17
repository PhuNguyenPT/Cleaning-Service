package com.example.cleaning_service.notifications.services;

import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.security.events.UserRoleUpdatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityNotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String SECURITY_NOTIFICATION_TOPIC = "security-notifications";

    public SecurityNotificationService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void handleUserRoleUpdatedEvent(UserRoleUpdatedEvent event) {
        SecurityNotification notification = new SecurityNotification(
                event.user(),
                "ROLE_UPDATED",
                "Your account permissions have been updated. Please refresh your session.",
                System.currentTimeMillis()
        );

        try {
            String notificationJson = objectMapper.writeValueAsString(notification);
            // Use user ID as key for partitioning
            kafkaTemplate.send(SECURITY_NOTIFICATION_TOPIC, event.user().getId().toString(), notificationJson);
            log.info("Sent role update notification to Kafka for user: {}", event.user().getUsername());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize security notification for user: {}", event.user().getUsername(), e);
        }
    }
}
