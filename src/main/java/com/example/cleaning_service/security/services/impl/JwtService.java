package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.entities.tokens.TokenEntity;
import com.example.cleaning_service.security.repositories.TokenRepository;
import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements IJwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public JwtService(TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public void saveToken(String token) {
        log.info("Saving token for authentication");
        String username = jwtUtil.extractUsername(token);
        Long expirationMillis = jwtUtil.extractExpirationTime(token);
        long timeToLive = expirationMillis - System.currentTimeMillis();
        if (timeToLive <= 0) {
            log.warn("Attempted to save expired token for user: {}", username);
        }

        log.debug("Token TTL: {} ms for user: {}", timeToLive, username);
        TokenEntity tokenEntity = new TokenEntity(token, username, timeToLive);
        TokenEntity savedEntity = tokenRepository.save(tokenEntity);
        log.info("Token successfully saved: {}", savedEntity);
    }

    @Transactional
    @Override
    public boolean existsTokenById(String token) {
        log.debug("Checking if token exists in repository");
        boolean exists = tokenRepository.existsById(token);
        if (!exists) {
            log.debug("Token not found in repository");
        }

        tokenRepository.findById(token).ifPresent(tokenEntity ->
                log.debug("Token found in repository: {}", tokenEntity));
        return exists;
    }

    @Transactional
    @Override
    public void logoutToken(String token) {
        log.info("Processing logout for token");
        tokenRepository.findById(token).ifPresent(tokenEntity -> {
            log.debug("Found token: {}, marking as blacklisted", tokenEntity);
            tokenEntity.setBlacklisted(true);
            TokenEntity updatedEntity = tokenRepository.save(tokenEntity);
            log.info("Token successfully blacklisted: {}", updatedEntity);
        });

        if (!tokenRepository.existsById(token)) {
            log.warn("Attempted to blacklist non-existent token");
        }
    }

    @Transactional
    @Override
    public boolean isTokenBlacklisted(String token) {
        log.debug("Checking if token is blacklisted");
        return tokenRepository.findById(token)
                .map(tokenEntity -> {
                    boolean isBlacklisted = tokenEntity.isBlacklisted();
                    log.debug("Token status: {}, Blacklisted: {}", tokenEntity, isBlacklisted);
                    return isBlacklisted;
                })
                .orElseGet(() -> {
                    log.debug("Token is blacklisted not found in repository");
                    return false;
                });
    }
}