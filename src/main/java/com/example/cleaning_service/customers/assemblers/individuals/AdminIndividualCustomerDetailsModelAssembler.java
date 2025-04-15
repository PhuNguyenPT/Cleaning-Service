package com.example.cleaning_service.customers.assemblers.individuals;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
import com.example.cleaning_service.customers.controllers.IndividualCustomerController;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminIndividualCustomerDetailsModelAssembler extends RepresentationModelAssemblerSupport<IndividualCustomer, IndividualCustomerDetailsResponseModel> {
    private final IndividualCustomerMapper individualCustomerMapper;

    public AdminIndividualCustomerDetailsModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        super(IndividualCustomerController.class, IndividualCustomerDetailsResponseModel.class);
        this.individualCustomerMapper = individualCustomerMapper;
    }

    @Override
    protected @NonNull IndividualCustomerDetailsResponseModel instantiateModel(@NonNull IndividualCustomer individualCustomer) {
        return individualCustomerMapper.fromCustomerToDetailsResponseModel(individualCustomer);
    }

    @Override
    public @NonNull IndividualCustomerDetailsResponseModel toModel(@NonNull IndividualCustomer individualCustomer) {
        IndividualCustomerDetailsResponseModel individualCustomerDetailsResponseModel = instantiateModel(individualCustomer);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminIndividualCustomerDetailsResponseModelById(individualCustomer.getId())).withSelfRel();
        individualCustomerDetailsResponseModel.add(selfLink);
        return individualCustomerDetailsResponseModel;
    }
}