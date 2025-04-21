package com.example.cleaning_service.security.config;

import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenLoginModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenModelAssembler;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegisterModel;
import com.example.cleaning_service.security.dtos.auth.TokenModel;
import com.example.cleaning_service.security.mapper.TokenEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelAssemblerConfig {
    @Bean
    public AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler() {
        return new AuthResponseRegisterModelAssembler(AuthController.class, AuthResponseRegisterModel.class);
    }

    @Bean
    public TokenModelAssembler tokenModelAssembler(TokenEntityMapper tokenEntityMapper) {
        return new TokenModelAssembler(AuthController.class, TokenModel.class, tokenEntityMapper);
    }

    @Bean
    public TokenLoginModelAssembler tokenLoginModelAssembler(TokenEntityMapper tokenEntityMapper) {
        return new TokenLoginModelAssembler(AuthController.class, TokenModel.class, tokenEntityMapper);
    }
}
