package com.example.cleaning_service.security.entities.token;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("token")
public class TokenEntity {
    @Id
    private String token;
    private String username;
    private boolean blacklisted;

    @TimeToLive
    private Long timeToLive;

    // Constructors
    public TokenEntity() {}

    public TokenEntity(String token, String username, Long timeToLive) {
        this.token = token;
        this.username = username;
        this.timeToLive = timeToLive;
        this.blacklisted = false;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
                "token='" + (token != null ? token.substring(0, Math.min(token.length(), 10)) + "..." : null) + '\'' +
                ", username='" + username + '\'' +
                ", blacklisted=" + blacklisted +
                ", timeToLive=" + timeToLive +
                '}';
    }
}
