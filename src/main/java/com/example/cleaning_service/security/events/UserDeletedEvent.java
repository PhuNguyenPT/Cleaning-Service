package com.example.cleaning_service.security.events;

import com.example.cleaning_service.security.entities.user.User;
import org.springframework.context.ApplicationEvent;

public record UserDeletedEvent(User user) {
}
