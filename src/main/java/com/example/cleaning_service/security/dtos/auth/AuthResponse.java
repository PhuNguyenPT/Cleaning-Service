package com.example.cleaning_service.security.dtos.auth;

public record AuthResponse(Long userId, String accessToken, Long expiresIn) {
}
