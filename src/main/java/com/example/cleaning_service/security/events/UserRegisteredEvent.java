package com.example.cleaning_service.security.events;

import com.example.cleaning_service.security.entities.user.User;

public record UserRegisteredEvent(User user) {
}
