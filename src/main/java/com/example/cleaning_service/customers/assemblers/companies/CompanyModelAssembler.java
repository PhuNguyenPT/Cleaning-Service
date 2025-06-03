package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyResponseModel> {
    private final CompanyMapper companyMapper;

    public CompanyModelAssembler(Class<?> controllerClass, Class<CompanyResponseModel> resourceType, CompanyMapper companyMapper) {
        super(controllerClass, resourceType);
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
