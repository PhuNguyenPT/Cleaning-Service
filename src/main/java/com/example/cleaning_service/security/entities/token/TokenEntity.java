package com.example.cleaning_service.security.entities.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Setter
@Getter
@RedisHash("token")
public class TokenEntity {
    @Id
    private UUID id;

    @Indexed
    private String token;

    private String username;
    private boolean blacklisted;

    @TimeToLive
    private Long timeToLive;

    public TokenEntity(String token, String username, Long timeToLive) {
        this.id = UUID.randomUUID();
        this.token = token;
        this.username = username;
        this.timeToLive = timeToLive;
        this.blacklisted = false;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
                "id=" + id +
                ", token='" + (token != null ? token.substring(0, Math.min(token.length(), 10)) + "..." : null) + '\'' +
                ", username='" + username + '\'' +
                ", blacklisted=" + blacklisted +
                ", timeToLive=" + timeToLive +
                '}';
    }
}
