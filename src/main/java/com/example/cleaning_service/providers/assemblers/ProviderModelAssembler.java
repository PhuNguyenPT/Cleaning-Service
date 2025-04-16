package com.example.cleaning_service.providers.assemblers;

import com.example.cleaning_service.providers.dtos.ProviderModel;
import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.mappers.ProviderMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProviderModelAssembler extends RepresentationModelAssemblerSupport<Provider, ProviderModel> {
    private final ProviderMapper providerMapper;

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public ProviderModelAssembler(Class<?> controllerClass, Class<ProviderModel> resourceType, ProviderMapper providerMapper) {
        super(controllerClass, resourceType);
        this.providerMapper = providerMapper;
    }


    @Override
    protected @NonNull ProviderModel instantiateModel(@NonNull Provider provider) {
        return providerMapper.toProviderModel(provider);
    }

    @Override
    public @NonNull ProviderModel toModel(@NonNull Provider provider) {
        return createModelWithId(provider.getId(), provider);
    }
}
