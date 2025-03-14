package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.companies.CompanyDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.CompanyResponseModelAssembler;
import com.example.cleaning_service.customers.dto.*;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyService {
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final AccountAssociationService accountAssociationService;
    private final CompanyResponseModelAssembler companyResponseModelAssembler;
    private final CompanyDetailsResponseModelAssembler companyDetailsResponseModelAssembler;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;

    public CompanyService(CompanyMapper companyMapper, CompanyRepository companyRepository, AccountAssociationService accountAssociationService, CompanyResponseModelAssembler companyResponseModelAssembler, CompanyDetailsResponseModelAssembler companyDetailsResponseModelAssembler, BusinessEntityService businessEntityService, AbstractCustomerService abstractCustomerService, OrganizationDetailsService organizationDetailsService) {
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
        this.accountAssociationService = accountAssociationService;
        this.companyResponseModelAssembler = companyResponseModelAssembler;
        this.companyDetailsResponseModelAssembler = companyDetailsResponseModelAssembler;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
    }

    @Transactional
    Company saveCompany(@NotNull Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public CompanyResponseModel createCompany(@NotNull CompanyRequest companyRequest, @NotNull User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        // Save the company
        Company company = companyMapper.fromCompanyRequestToCompany(companyRequest);
        Company savedCompany = saveCompany(company);

        // Create account association
        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(user, savedCompany);
        AccountAssociation accountAssociation = accountAssociationService.createAccountAssociation(accountAssociationRequest);

        // Ensure the account association correctly references a company
        if (!(accountAssociation.getCustomer() instanceof Company accountAssociationCustomerCompany)) {
            throw new IllegalStateException("Account association does not reference a valid company.");
        }

        return companyResponseModelAssembler.toModel(accountAssociationCustomerCompany);
    }

    @Transactional
    Company getByIdAndUser(UUID id, User user) {
        if (!accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with a company.");
        }
        return findById(id);
    }

    @Transactional
    public CompanyDetailsResponseModel getCompanyDetailsResponseModelById(UUID id, User user) {
        Company dbCompany = getByIdAndUser(id, user);
        return companyDetailsResponseModelAssembler.toModel(dbCompany);
    }

    @Transactional
    Company findById(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Company with id " + id + " not found."));
    }

    @Transactional
    public CompanyDetailsResponseModel updateCompanyDetails(UUID id, @Valid CompanyUpdateRequest updateRequest, User user) {
        Company dbCompany = getByIdAndUser(id, user);

        updateCompanyFields(dbCompany, updateRequest);
        Company updatedCompany = saveCompany(dbCompany);
        return companyDetailsResponseModelAssembler.toModel(updatedCompany);
    }

    /**
     * Updates only the non-null fields of the company.
     */
    @Transactional
    void updateCompanyFields(Company company, @Valid CompanyUpdateRequest companyRequest) {
        if (companyRequest.companyType() != null) {
            company.setCompanyType(companyRequest.companyType());
        }

        organizationDetailsService.updateOrganizationDetails(company, companyRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(company, companyRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(company, companyRequest.businessEntityDetails());
    }

    @Transactional
    public void deleteCompanyById(UUID id, User user) {
        Company dbCompany = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(dbCompany);
        companyRepository.delete(dbCompany);
    }
}
