package com.example.cleaning_service.customers.assemblers.non_profit_org;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
import com.example.cleaning_service.customers.controllers.NonProfitOrgController;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminNonProfitOrgDetailsModelAssembler  extends RepresentationModelAssemblerSupport<NonProfitOrg, NonProfitOrgDetailsResponseModel> {

    private final NonProfitOrgMapper nonProfitOrgMapper;

    public AdminNonProfitOrgDetailsModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        super(NonProfitOrgController.class, NonProfitOrgDetailsResponseModel.class);
        this.nonProfitOrgMapper = nonProfitOrgMapper;
    }

    @Override
    protected @NonNull NonProfitOrgDetailsResponseModel instantiateModel(@NonNull NonProfitOrg nonProfitOrg) {
        return nonProfitOrgMapper.fromNonProfitOrgToDetailsModel(nonProfitOrg);
    }

    @Override
    public @NonNull NonProfitOrgDetailsResponseModel toModel(@NonNull NonProfitOrg nonProfitOrg) {
        NonProfitOrgDetailsResponseModel nonProfitOrgDetailsResponseModel = instantiateModel(nonProfitOrg);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminNonProfitOrgDetailsResponseModelById(nonProfitOrg.getId())).withSelfRel();
        nonProfitOrgDetailsResponseModel.add(selfLink);
        return nonProfitOrgDetailsResponseModel;
    }
}
