package com.example.cleaning_service.customers.assemblers.non_profit_org;

import com.example.cleaning_service.customers.controllers.NonProfitOrgController;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NonProfitOrgResponseModelAssembler extends RepresentationModelAssemblerSupport<NonProfitOrg, NonProfitOrgResponseModel> {

    private final NonProfitOrgMapper nonProfitOrgMapper;

    public NonProfitOrgResponseModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        super(NonProfitOrgController.class, NonProfitOrgResponseModel.class);
        this.nonProfitOrgMapper = nonProfitOrgMapper;
    }

    @Override
    protected @NonNull NonProfitOrgResponseModel instantiateModel(@NonNull NonProfitOrg nonProfitOrg) {
        return nonProfitOrgMapper.fromNonProfitOrgToModel(nonProfitOrg);
    }

    @Override
    public @NonNull NonProfitOrgResponseModel toModel(@NonNull NonProfitOrg nonProfitOrg) {
        return createModelWithId(nonProfitOrg.getId(), nonProfitOrg);
    }
}
