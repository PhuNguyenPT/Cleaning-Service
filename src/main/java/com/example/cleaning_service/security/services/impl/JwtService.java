package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.repositories.TokenRepository;
import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService implements IJwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private static final long BLACKLIST_TTL = 300; // 5 minutes for blacklisted tokens

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public JwtService(TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public TokenEntity saveToken(String token) {
        log.info("Saving token for authentication");
        String username = jwtUtil.extractUsername(token);
        Long expirationMillis = jwtUtil.extractExpirationTime(token);

        // Debug info
        Date expirationDate = new Date(expirationMillis);
        log.info("JWT expiration timestamp: {}, which is date: {}", expirationMillis, expirationDate);

        // Calculate remaining time until expiration
        long currentTimeMillis = System.currentTimeMillis();
        log.info("Current timestamp: {}, which is date: {}", currentTimeMillis, new Date(currentTimeMillis));

        long timeToLive = expirationMillis - currentTimeMillis;
        log.info("Calculated TTL: {} ms", timeToLive);

        if (timeToLive <= 0) {
            log.warn("Attempted to save expired token for user: {}", username);
            throw new IllegalArgumentException("Expired JWT token");
        }

        log.info("Token TTL: {} ms for user: {}", timeToLive, username);
        TokenEntity tokenEntity = new TokenEntity(token, username, timeToLive);
        TokenEntity savedEntity = tokenRepository.save(tokenEntity);
        log.info("Token successfully saved: {}", savedEntity);
        return savedEntity;
    }

    @Transactional
    @Override
    public void logoutToken(String token) {
        log.info("Processing logout for token");
        TokenEntity tokenEntity = findByToken(token);
        log.info("Found token: {}, marking as blacklisted", tokenEntity);

        // Reduce TTL to a short duration for blacklisted tokens
        tokenEntity.setBlacklisted(true);
        tokenEntity.setTimeToLive(BLACKLIST_TTL); // Set a short TTL for blacklisted tokens

        TokenEntity savedEntity = tokenRepository.save(tokenEntity);
        log.info("Token successfully blacklisted: {}", savedEntity);
    }

    @Transactional
    @Override
    public boolean existsByToken(String token) {
        log.debug("Checking if token exists in repository");
        boolean exists = tokenRepository.existsByToken(token);
        if (!exists) {
            log.info("Token does not exist: {}...", token.substring(0, 10));
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public TokenEntity findByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new AccessDeniedException("Token not found"));
    }

    @Transactional
    @Override
    public TokenEntity findById(UUID id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new AccessDeniedException("Token with id " + id + "not found"));
    }

    @Transactional
    @Override
    public boolean isTokenBlacklisted(String token) {
        log.debug("Checking if token is blacklisted");
        TokenEntity tokenEntity = findByToken(token);
        log.info("Found token: {}, blacklisted: {}", token.substring(0,20), tokenEntity.isBlacklisted());
        return tokenEntity.isBlacklisted();
    }
}