package com.example.cleaning_service.security.events;

import com.example.cleaning_service.security.entities.user.User;

public record UserDeletedEvent(User user) {
}
