package com.example.cleaning_service.customers.assemblers.governments;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
import com.example.cleaning_service.customers.controllers.GovernmentController;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminGovernmentDetailsModelAssembler extends RepresentationModelAssemblerSupport<Government, GovernmentDetailsResponseModel> {

    private final GovernmentMapper governmentMapper;

    public AdminGovernmentDetailsModelAssembler(GovernmentMapper governmentMapper) {
        super(GovernmentController.class, GovernmentDetailsResponseModel.class);
        this.governmentMapper = governmentMapper;
    }

    @Override
    protected @NonNull GovernmentDetailsResponseModel instantiateModel(@NonNull Government government) {
        return governmentMapper.fromGovernmentToGovernmentDetailsResponseModel(government);
    }

    @Override
    public @NonNull GovernmentDetailsResponseModel toModel(@NonNull Government government) {
        GovernmentDetailsResponseModel governmentDetailsResponseModel = instantiateModel(government);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminGovernmentDetailsResponseModelById(government.getId())).withSelfRel();
        governmentDetailsResponseModel.add(selfLink);
        return governmentDetailsResponseModel;
    }
}