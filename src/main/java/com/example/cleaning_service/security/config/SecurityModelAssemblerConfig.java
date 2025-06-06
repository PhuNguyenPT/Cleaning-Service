package com.example.cleaning_service.security.config;

import com.example.cleaning_service.security.assemblers.AuthResponseProfileModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenLoginModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenModelAssembler;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseProfileModel;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegisterModel;
import com.example.cleaning_service.security.dtos.auth.TokenModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import com.example.cleaning_service.security.mapper.TokenEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
public class SecurityModelAssemblerConfig {

    @Bean
    public AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler(AuthMapper authMapper) {
        return new AuthResponseRegisterModelAssembler(AuthController.class, AuthResponseRegisterModel.class, authMapper);
    }

    @Bean
    public TokenModelAssembler tokenModelAssembler(TokenEntityMapper tokenEntityMapper) {
        return new TokenModelAssembler(AuthController.class, TokenModel.class, tokenEntityMapper);
    }

    @Bean
    public TokenLoginModelAssembler tokenLoginModelAssembler(TokenEntityMapper tokenEntityMapper) {
        return new TokenLoginModelAssembler(AuthController.class, TokenModel.class, tokenEntityMapper);
    }

    @Bean
    public AuthResponseProfileModelAssembler authResponseProfileModelAssembler(AuthMapper authMapper) {
        return new AuthResponseProfileModelAssembler(AuthController.class, AuthResponseProfileModel.class, authMapper);
    }

    @Bean
    public PagedResourcesAssembler<User> pagedResourcesAssemblerUser(PagedResourcesAssembler<User> pagedResourcesAssembler) {
        return pagedResourcesAssembler;
    }
}
