package com.example.cleaning_service.notifications.entites;

import com.example.cleaning_service.security.entities.user.User;

public record SecurityNotification(
        User user,
        String type,
        String message,
        long timestamp
) {

}
