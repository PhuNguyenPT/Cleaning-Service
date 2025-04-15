package com.example.cleaning_service.customers.assemblers.individuals;

import com.example.cleaning_service.customers.controllers.IndividualCustomerController;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class IndividualCustomerDetailsModelAssembler extends RepresentationModelAssemblerSupport<IndividualCustomer, IndividualCustomerDetailsResponseModel> {
    private final IndividualCustomerMapper individualCustomerMapper;

    public IndividualCustomerDetailsModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        super(IndividualCustomerController.class, IndividualCustomerDetailsResponseModel.class);
        this.individualCustomerMapper = individualCustomerMapper;
    }

    @Override
    protected @NonNull IndividualCustomerDetailsResponseModel instantiateModel(@NonNull IndividualCustomer individualCustomer) {
        return individualCustomerMapper.fromCustomerToDetailsResponseModel(individualCustomer);
    }

    @Override
    public @NonNull IndividualCustomerDetailsResponseModel toModel(@NonNull IndividualCustomer individualCustomer) {
        return createModelWithId(individualCustomer.getId(), individualCustomer);
    }
}
