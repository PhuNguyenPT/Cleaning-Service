package com.example.cleaning_service.security.dtos.auth;

import java.util.UUID;

public record AuthResponse(UUID userId, String accessToken, Long expiresIn) {
}
