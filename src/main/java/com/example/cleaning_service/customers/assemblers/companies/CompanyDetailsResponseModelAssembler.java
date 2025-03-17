package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.controllers.CompanyController;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDetailsResponseModel> {

    private static final Logger log = LoggerFactory.getLogger(CompanyDetailsResponseModelAssembler.class);
    private final CompanyMapper companyMapper;

    public CompanyDetailsResponseModelAssembler(CompanyMapper companyMapper) {
        super(CompanyController.class, CompanyDetailsResponseModel.class);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyDetailsResponseModel instantiateModel(@NonNull Company entity) {
        CompanyDetailsResponseModel companyDetailsResponseModel = companyMapper.fromCompanyToCompanyDetailsResponseModel(entity);
        log.info("Retrieved company details response: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
    }

    @Override
    public @NonNull CompanyDetailsResponseModel toModel(@NonNull Company entity) {
        CompanyDetailsResponseModel companyDetailsResponseModel = createModelWithId(entity.getId(), entity);
        log.info("Created company details response: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
    }
}
