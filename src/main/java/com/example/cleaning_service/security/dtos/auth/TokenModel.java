package com.example.cleaning_service.security.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class TokenModel extends RepresentationModel<TokenModel> {
    private final String accessToken;
    private final Long expiresIn;
}
