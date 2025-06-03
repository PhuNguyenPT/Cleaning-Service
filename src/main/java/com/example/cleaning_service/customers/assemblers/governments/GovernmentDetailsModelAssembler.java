package com.example.cleaning_service.customers.assemblers.governments;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GovernmentDetailsModelAssembler extends RepresentationModelAssemblerSupport<Government, GovernmentDetailsResponseModel> {

    private final GovernmentMapper governmentMapper;

    public GovernmentDetailsModelAssembler(Class<?> controllerClass, Class<GovernmentDetailsResponseModel> resourceType, GovernmentMapper governmentMapper) {
        super(controllerClass, resourceType);
        this.governmentMapper = governmentMapper;
    }

    @Override
    protected @NonNull GovernmentDetailsResponseModel instantiateModel(@NonNull Government government) {
        return governmentMapper.fromGovernmentToGovernmentDetailsResponseModel(government);
    }

    @Override
    public @NonNull GovernmentDetailsResponseModel toModel(@NonNull Government government) {
        return createModelWithId(government.getId(), government);
    }
}
