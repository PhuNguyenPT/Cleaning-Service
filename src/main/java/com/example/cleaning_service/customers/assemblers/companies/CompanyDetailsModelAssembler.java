package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDetailsResponseModel> {
    private final CompanyMapper companyMapper;

    public CompanyDetailsModelAssembler(Class<?> controllerClass, Class<CompanyDetailsResponseModel> resourceType, CompanyMapper companyMapper) {
        super(controllerClass, resourceType);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyDetailsResponseModel instantiateModel(@NonNull Company company) {
        return companyMapper.fromCompanyToCompanyDetailsResponseModel(company);
    }

    @Override
    public @NonNull CompanyDetailsResponseModel toModel(@NonNull Company company) {
        return createModelWithId(company.getId(), company);
    }
}
