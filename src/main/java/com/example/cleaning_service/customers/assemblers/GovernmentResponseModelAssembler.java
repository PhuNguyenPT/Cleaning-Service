package com.example.cleaning_service.customers.assemblers;

import com.example.cleaning_service.customers.controllers.GovernmentController;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GovernmentResponseModelAssembler extends  RepresentationModelAssemblerSupport<Government, GovernmentResponseModel> {

    private final GovernmentMapper governmentMapper;

    public GovernmentResponseModelAssembler(GovernmentMapper governmentMapper) {
        super(GovernmentController.class, GovernmentResponseModel.class);
        this.governmentMapper = governmentMapper;
    }

    @Override
    protected @NonNull GovernmentResponseModel instantiateModel(@NonNull Government government) {
        return governmentMapper.fromGovernmentToGovernmentResponseModel(government);
    }

    @Override
    public @NonNull GovernmentResponseModel toModel(@NonNull Government government) {
        return createModelWithId(government.getId(), government);
    }
}
