package com.example.cleaning_service.customers.assemblers.non_profit_org;

import com.example.cleaning_service.customers.controllers.NonProfitOrgController;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NonProfitOrgDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<NonProfitOrg, NonProfitOrgDetailsResponseModel> {

    private final NonProfitOrgMapper nonProfitOrgMapper;

    public NonProfitOrgDetailsResponseModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        super(NonProfitOrgController.class, NonProfitOrgDetailsResponseModel.class);
        this.nonProfitOrgMapper = nonProfitOrgMapper;
    }

    @Override
    protected @NonNull NonProfitOrgDetailsResponseModel instantiateModel(@NonNull NonProfitOrg nonProfitOrg) {
        return nonProfitOrgMapper.fromNonProfitOrgToDetailsModel(nonProfitOrg);
    }

    @Override
    public @NonNull NonProfitOrgDetailsResponseModel toModel(@NonNull NonProfitOrg nonProfitOrg) {
        return createModelWithId(nonProfitOrg.getId(), nonProfitOrg);
    }
}
