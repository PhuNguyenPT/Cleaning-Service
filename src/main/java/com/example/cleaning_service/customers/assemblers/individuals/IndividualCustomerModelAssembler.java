package com.example.cleaning_service.customers.assemblers.individuals;

import com.example.cleaning_service.customers.controllers.IndividualCustomerController;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class IndividualCustomerModelAssembler extends RepresentationModelAssemblerSupport<IndividualCustomer, IndividualCustomerResponseModel> {
    private final IndividualCustomerMapper individualCustomerMapper;

    public IndividualCustomerModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        super(IndividualCustomerController.class, IndividualCustomerResponseModel.class);
        this.individualCustomerMapper = individualCustomerMapper;
    }

    @Override
    protected @NonNull IndividualCustomerResponseModel instantiateModel(@NonNull IndividualCustomer individualCustomer) {
        return individualCustomerMapper.fromIndividualToResponseModel(individualCustomer);
    }

    @Override
    public @NonNull IndividualCustomerResponseModel toModel(@NonNull IndividualCustomer individualCustomer) {
        return createModelWithId(individualCustomer.getId(), individualCustomer);
    }
}
