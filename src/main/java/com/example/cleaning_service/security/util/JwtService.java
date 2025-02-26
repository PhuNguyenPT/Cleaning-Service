package com.example.cleaning_service.security.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public JwtService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    public void saveToken(String token) {
        String username;
        Long expirationMillis;
        try {
            username = jwtUtil.extractUsername(token);
            expirationMillis = jwtUtil.extractExpirationTime(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(token, username, expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenSaved(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public void logoutToken(String token) {
        try {
            Long expirationMillis = jwtUtil.extractExpirationTime(token); // Extract token expiration time
            long expirationTime = expirationMillis - System.currentTimeMillis(); // Remaining time

            if (expirationTime > 0) {
                // Store "blacklisted" in Redis with TTL set to token's remaining validity
                redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        String value = redisTemplate.opsForValue().get(token);
        return "blacklisted".equals(value);
    }
}
