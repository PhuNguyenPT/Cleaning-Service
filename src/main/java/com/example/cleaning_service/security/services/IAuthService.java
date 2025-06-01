package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface IAuthService {
    User register(AuthRequest authRequest);
    TokenEntity login(AuthRequest authRequest);
    void logout(String token);
    TokenEntity refreshToken(String token, User user);
    TokenEntity getTokenById(UUID tokenId, User user);
}
