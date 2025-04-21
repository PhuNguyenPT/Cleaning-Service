package com.example.cleaning_service.notifications.entites;

import java.util.UUID;

public record SecurityNotification(
        UUID userId,
        String userName,
        String type,
        String message,
        long timestamp
) {

}
