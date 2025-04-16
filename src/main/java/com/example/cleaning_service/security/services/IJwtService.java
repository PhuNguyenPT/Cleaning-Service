package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.entities.token.TokenEntity;

import java.util.UUID;

public interface IJwtService {
    TokenEntity saveToken(String token);
    boolean existsByToken(String token);
    void logoutToken(String token);

    TokenEntity findById(UUID id);
    TokenEntity findByToken(String token);
    boolean isTokenBlacklisted(String token);
}
