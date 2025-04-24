package com.example.cleaning_service.notifications.services;

import com.example.cleaning_service.notifications.entites.SecurityNotification;
import com.example.cleaning_service.security.events.UserRoleUpdatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class SecurityNotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String SECURITY_NOTIFICATION_TOPIC = "security-notifications";

    public SecurityNotificationService(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRoleUpdatedEvent(UserRoleUpdatedEvent event) {
        SecurityNotification notification = new SecurityNotification(
                event.user().getId(),
                event.user().getUsername(),
                "ROLE_UPDATED",
                "Your account permissions have been updated. Please refresh your session.",
                System.currentTimeMillis()
        );

        try {
            String notificationJson = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(SECURITY_NOTIFICATION_TOPIC, event.user().getId().toString(), notificationJson);
            log.info("Sent role update notification to Kafka for user: {}", event.user().getUsername());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize security notification for user: {}", event.user().getUsername(), e);
        }
    }
}
