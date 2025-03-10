package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtService implements IJwtService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public JwtService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public void saveToken(String token) { // Let exception propagate
        String username = jwtUtil.extractUsername(token);
        Long expirationMillis = jwtUtil.extractExpirationTime(token);
        redisTemplate.opsForValue().set(token, username, expirationMillis, TimeUnit.MILLISECONDS);
    }

    @Transactional
    @Override
    public boolean isTokenSaved(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    @Transactional
    @Override
    public void logoutToken(String token) { // Let exception propagate
        Long expirationMillis = jwtUtil.extractExpirationTime(token);
        long expirationTime = expirationMillis - System.currentTimeMillis();

        if (expirationTime > 0) {
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
        }
    }

    @Transactional
    @Override
    public boolean isTokenBlacklisted(String token) {
        return "blacklisted".equals(redisTemplate.opsForValue().get(token));
    }
}