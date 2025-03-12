package com.example.cleaning_service.customers.assemblers;

import com.example.cleaning_service.customers.controllers.CompanyController;
import com.example.cleaning_service.customers.dto.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDetailsResponseModel> {

    private final CompanyMapper companyMapper;

    public CompanyDetailsResponseModelAssembler(CompanyMapper companyMapper) {
        super(CompanyController.class, CompanyDetailsResponseModel.class);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyDetailsResponseModel instantiateModel(@NonNull Company entity) {
        return companyMapper.fromCompanyToCompanyDetailsResponseModel(entity);
    }

    @Override
    public @NonNull CompanyDetailsResponseModel toModel(@NonNull Company entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
