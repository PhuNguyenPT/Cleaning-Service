package com.example.cleaning_service.providers.assemblers;

import com.example.cleaning_service.providers.dtos.ProviderDetailsModel;
import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.mappers.ProviderMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProviderDetailsModelAssembler extends RepresentationModelAssemblerSupport<Provider, ProviderDetailsModel> {
    private final ProviderMapper providerMapper;

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public ProviderDetailsModelAssembler(Class<?> controllerClass, Class<ProviderDetailsModel> resourceType, ProviderMapper providerMapper) {
        super(controllerClass, resourceType);
        this.providerMapper = providerMapper;
    }


    @Override
    protected @NonNull ProviderDetailsModel instantiateModel(@NonNull Provider provider) {
        return providerMapper.toProviderDetailsModel(provider);
    }

    @Override
    public @NonNull ProviderDetailsModel toModel(@NonNull Provider provider) {
        return createModelWithId(provider.getId(), provider);
    }
}
