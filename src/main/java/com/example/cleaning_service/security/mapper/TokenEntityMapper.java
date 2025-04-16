package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.auth.TokenModel;
import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.util.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class TokenEntityMapper {
    private final JwtUtil jwtUtil;

    public TokenEntityMapper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public TokenModel toModel(TokenEntity tokenEntity) {
        return TokenModel.builder()
                .accessToken(tokenEntity.getToken())
                .expiresIn(jwtUtil.getDefaultExpirationTimeInSeconds())
                .build();
    }
}
