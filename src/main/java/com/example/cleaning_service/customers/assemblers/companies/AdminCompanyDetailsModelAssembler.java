package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminCompanyDetailsModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDetailsResponseModel> {
    private final CompanyMapper companyMapper;

    public AdminCompanyDetailsModelAssembler(CompanyMapper companyMapper) {
        super(AdminCustomerController.class, CompanyDetailsResponseModel.class);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyDetailsResponseModel instantiateModel(@NonNull Company entity) {
        return companyMapper.fromCompanyToCompanyDetailsResponseModel(entity);
    }

    @Override
    public @NonNull CompanyDetailsResponseModel toModel(@NonNull Company company) {
        CompanyDetailsResponseModel companyDetailsResponseModel = instantiateModel(company);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminCompanyDetailsResponseModelById(company.getId())).withSelfRel();
        companyDetailsResponseModel.add(selfLink);
        return companyDetailsResponseModel;
    }
}