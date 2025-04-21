package com.example.cleaning_service.providers.events;

import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.security.entities.user.User;

public record ProviderCreatedEvent(Provider provider, User user) {
}
