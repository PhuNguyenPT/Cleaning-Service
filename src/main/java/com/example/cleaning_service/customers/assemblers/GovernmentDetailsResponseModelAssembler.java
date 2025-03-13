package com.example.cleaning_service.customers.assemblers;

import com.example.cleaning_service.customers.controllers.GovernmentController;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;

public class GovernmentDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<Government, GovernmentDetailsResponseModel> {

    private final GovernmentMapper governmentMapper;

    public GovernmentDetailsResponseModelAssembler(GovernmentMapper governmentMapper) {
        super(GovernmentController.class, GovernmentDetailsResponseModel.class);
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
