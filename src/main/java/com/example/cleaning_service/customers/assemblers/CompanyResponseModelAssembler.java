package com.example.cleaning_service.customers.assemblers;

import com.example.cleaning_service.customers.controllers.CompanyController;
import com.example.cleaning_service.customers.dto.CompanyResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyResponseModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyResponseModel> {
    private final CompanyMapper companyMapper;

    public CompanyResponseModelAssembler(CompanyMapper companyMapper) {
        super(CompanyController.class, CompanyResponseModel.class);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyResponseModel instantiateModel(@NonNull Company entity) {
        return companyMapper.fromCompanyToCompanyResponseModel(entity);
    }

    @Override
    public @NonNull CompanyResponseModel toModel(@NonNull Company entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
