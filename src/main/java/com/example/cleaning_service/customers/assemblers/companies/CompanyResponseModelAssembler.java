package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.controllers.CompanyController;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyResponseModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyResponseModel> {
    private static final Logger log = LoggerFactory.getLogger(CompanyResponseModelAssembler.class);
    private final CompanyMapper companyMapper;

    public CompanyResponseModelAssembler(CompanyMapper companyMapper) {
        super(CompanyController.class, CompanyResponseModel.class);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyResponseModel instantiateModel(@NonNull Company entity) {
        CompanyResponseModel companyResponseModel = companyMapper.fromCompanyToCompanyResponseModel(entity);
        log.info("Retrieved company details response: {}", companyResponseModel);
        return companyResponseModel;
    }

    @Override
    public @NonNull CompanyResponseModel toModel(@NonNull Company entity) {
        CompanyResponseModel companyResponseModel = createModelWithId(entity.getId(), entity);
        log.info("Created company details response: {}", companyResponseModel);
        return companyResponseModel;
    }
}
