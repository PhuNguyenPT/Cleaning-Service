package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.TokenModel;
import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.mapper.TokenEntityMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class TokenLoginModelAssembler extends RepresentationModelAssemblerSupport<TokenEntity, TokenModel> {

    private final TokenEntityMapper tokenEntityMapper;

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public TokenLoginModelAssembler(Class<?> controllerClass, Class<TokenModel> resourceType, TokenEntityMapper tokenEntityMapper) {
        super(controllerClass, resourceType);
        this.tokenEntityMapper = tokenEntityMapper;
    }

    @Override
    protected @NonNull TokenModel instantiateModel(@NonNull TokenEntity tokenEntity) {
        return tokenEntityMapper.toModel(tokenEntity);
    }

    @Override
    public @NonNull TokenModel toModel(@NonNull TokenEntity tokenEntity) {
        TokenModel tokenModel = createModelWithId("me", tokenEntity);

        Link logoutLink = linkTo(AuthController.class).slash("logout").withRel("logout");
        tokenModel.add(logoutLink);

        return tokenModel;
    }
}
