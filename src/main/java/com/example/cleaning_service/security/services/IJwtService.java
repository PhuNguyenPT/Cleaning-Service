package com.example.cleaning_service.security.services;

public interface IJwtService {
    void saveToken(String token);
    boolean isTokenSaved(String token);
    void logoutToken(String token);
    boolean isTokenBlacklisted(String token);
}
