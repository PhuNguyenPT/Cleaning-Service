package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.assemblers.CompanyDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.CompanyResponseModelAssembler;
import com.example.cleaning_service.customers.dto.*;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
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

    public CompanyService(CompanyMapper companyMapper, CompanyRepository companyRepository, AccountAssociationService accountAssociationService, CompanyResponseModelAssembler companyResponseModelAssembler, CompanyDetailsResponseModelAssembler companyDetailsResponseModelAssembler) {
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
        this.accountAssociationService = accountAssociationService;
        this.companyResponseModelAssembler = companyResponseModelAssembler;
        this.companyDetailsResponseModelAssembler = companyDetailsResponseModelAssembler;
    }

    @Transactional
    protected Company saveCompanyByCompanyRequest(@NotNull CompanyRequest companyRequest) {
        Company company = companyMapper.fromCompanyRequestToCompany(companyRequest);
        return companyRepository.save(company);
    }

    @Transactional
    public CompanyResponseModel createCompany(@NotNull CompanyRequest companyRequest, @NotNull User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with a company.");
        }

        // Save the company
        Company savedCompany = saveCompanyByCompanyRequest(companyRequest);


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
    protected Company getByIdAndUser(UUID id, User user) {
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
    protected Company findById(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Company with id " + id + " not found."));
    }

    @Transactional
    public CompanyDetailsResponseModel updateCompanyDetails(UUID id, CompanyUpdateRequest updateRequest, User user) {
        Company dbCompany = getByIdAndUser(id, user);

        updateCompanyFields(dbCompany, updateRequest);
        Company updatedCompany = saveCompany(dbCompany);
        return companyDetailsResponseModelAssembler.toModel(updatedCompany);
    }

    @Transactional
    protected Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    /**
     * Updates only the non-null fields of the company.
     */
    @Transactional
    protected void updateCompanyFields(Company dbCompany, CompanyUpdateRequest companyRequest) {
        if (companyRequest.companyType() != null) {
            dbCompany.setCompanyType(companyRequest.companyType());
        }
        if (companyRequest.taxId() != null) {
            dbCompany.setTaxId(companyRequest.taxId());
        }
        if (companyRequest.registrationNumber() != null) {
            dbCompany.setRegistrationNumber(companyRequest.registrationNumber());
        }
        if (companyRequest.billingAddress() != null) {
            dbCompany.setBillingAddress(companyRequest.billingAddress());
        }
        if (companyRequest.paymentMethod() != null) {
            dbCompany.setPaymentMethod(companyRequest.paymentMethod());
        }
        if (companyRequest.preferredDays() != null) {
            dbCompany.setPreferredDays(companyRequest.preferredDays());
        }
        if (companyRequest.companyName() != null) {
            dbCompany.setName(companyRequest.companyName());
        }
        if (companyRequest.address() != null) {
            dbCompany.setAddress(companyRequest.address());
        }
        if (companyRequest.phone() != null) {
            dbCompany.setPhone(companyRequest.phone());
        }
        if (companyRequest.email() != null) {
            dbCompany.setEmail(companyRequest.email());
        }
        if (companyRequest.city() != null) {
            dbCompany.setCity(companyRequest.city());
        }
        if (companyRequest.state() != null) {
            dbCompany.setState(companyRequest.state());
        }
        if (companyRequest.zip() != null) {
            dbCompany.setZip(companyRequest.zip());
        }
        if (companyRequest.country() != null) {
            dbCompany.setCountry(companyRequest.country());
        }
        if (companyRequest.notes() != null) {
            dbCompany.setNotes(companyRequest.notes());
        }
    }
}
