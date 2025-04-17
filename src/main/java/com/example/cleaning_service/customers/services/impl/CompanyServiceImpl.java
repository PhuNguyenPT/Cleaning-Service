package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.companies.AdminCompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.CompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.CompanyModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.events.CustomerCreationEvent;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.customers.services.*;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of the CompanyService interface for managing Company entities.
 * <p>
 * This service handles the core business logic for company-related operations including
 * creation, retrieval, update, and deletion of company records. It coordinates with various
 * other services to ensure data integrity and proper association between companies, accounts,
 * and users within the cleaning service system.
 */
@Service
class CompanyServiceImpl implements CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;

    private final AccountService accountService;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;
    private final CustomerService customerService;

    private final CompanyModelAssembler companyModelAssembler;
    private final CompanyDetailsModelAssembler companyDetailsModelAssembler;

    private final CompanyMapper companyMapper;
    private final AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs a new CompanyServiceImpl with all required dependencies.
     *
     * @param companyRepository Repository for company data access
     * @param accountService Service for account management
     * @param businessEntityService Service for business entity operations
     * @param abstractCustomerService Service for customer operations
     * @param organizationDetailsService Service for organization details
     * @param customerService Service for general customer operations
     * @param companyModelAssembler Assembler for basic company models
     * @param companyDetailsModelAssembler Assembler for detailed company models
     * @param companyMapper Mapper for company entities and DTOs
     * @param adminCompanyDetailsModelAssembler Assembler for administrative company details
     */
    CompanyServiceImpl(
            CompanyRepository companyRepository,
            AccountService accountService,
            BusinessEntityService businessEntityService,
            AbstractCustomerService abstractCustomerService,
            OrganizationDetailsService organizationDetailsService,
            CustomerService customerService,
            CompanyModelAssembler companyModelAssembler,
            CompanyDetailsModelAssembler companyDetailsModelAssembler,
            CompanyMapper companyMapper,
            AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler, ApplicationEventPublisher applicationEventPublisher) {

        this.companyRepository = companyRepository;
        this.accountService = accountService;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
        this.customerService = customerService;
        this.companyModelAssembler = companyModelAssembler;
        this.companyDetailsModelAssembler = companyDetailsModelAssembler;
        this.companyMapper = companyMapper;
        this.adminCompanyDetailsModelAssembler = adminCompanyDetailsModelAssembler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public CompanyResponseModel createCompany(@Valid CompanyRequest companyRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                companyRequest,
                companyRepository::existsByTaxId,
                companyRepository::existsByRegistrationNumber,
                companyRepository::existsByEmail
        );

        log.info("Attempting to create a company for user: {}", user.getUsername());
        accountService.checkAccountReferenceCustomer(user);

        // Save the company
        Company company = companyMapper.fromCompanyRequestToCompany(companyRequest);
        Company savedCompany = saveCompany(company);
        log.info("Company {} saved successfully with ID: {}", savedCompany.getName(), savedCompany.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedCompany);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedCompany);

        // Update account
        AccountRequest accountRequest = new AccountRequest(
                user, savedCompany, null, isPrimary, associationType
        );
        CustomerCreationEvent customerCreationEvent = new CustomerCreationEvent(accountRequest);
        applicationEventPublisher.publishEvent(customerCreationEvent);

//        Account account = accountService.handleCustomerCreation(accountRequest);
//        // Ensure the account correctly references a company
//        if (isNotValidReferenceAbstractCustomer(account.getCustomer().getId(), account.getCustomer())) {
//            throw new IllegalStateException("Account does not reference a valid company.");
//        }
//
//        CompanyResponseModel companyResponseModel = companyModelAssembler.toModel((Company) account.getCustomer());
        CompanyResponseModel companyResponseModel = companyModelAssembler.toModel(savedCompany);
        log.info("Successfully created company response: {}", companyResponseModel);

        return companyResponseModel;
    }

    /**
     * Persists a company entity to the database.
     * <p>
     * This method handles the actual database operation for saving a company entity,
     * generating appropriate log entries for tracking the operation.
     *
     * @param company The company entity to persist
     * @return The saved {@link Company} entity with generated ID and metadata
     */
    @Transactional
    Company saveCompany(Company company) {
        log.info("Saving company: {}", company);
        Company savedCompany = companyRepository.save(company);
        log.info("Company saved with ID: {}", savedCompany.getId());
        return savedCompany;
    }

    @Override
    @Transactional
    public CompanyDetailsResponseModel getCompanyDetailsResponseModelById(UUID id, User user) {
        log.info("Retrieving company details for ID: {} by user: {}", id, user.getUsername());
        Company dbCompany = getByIdAndUser(id, user);
        CompanyDetailsResponseModel companyDetailsResponseModel = companyDetailsModelAssembler.toModel(dbCompany);
        log.info("Retrieved company details: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
    }

    /**
     * Retrieves a company by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Retrieves the user's account and associated customer</li>
     * <li>Checks if the user is associated with the requested company through their account</li>
     * <li>Returns the company if the user has proper access</li>
     * </ol>
     *
     * @param id The UUID of the company to retrieve
     * @param user The user requesting access to the company
     * @return The {@link Company} entity if found and accessible by the user
     * @throws AccessDeniedException If the user is not associated with the requested company
     */
    @Transactional
    Company getByIdAndUser(UUID id, User user) {
        log.info("Fetching company with ID: {} for user: {}", id, user.getUsername());
        AbstractCustomer abstractCustomer = accountService.findAccountWithCustomerByUser(user).getCustomer();
        if (isNotValidReferenceAbstractCustomer(id, abstractCustomer)) {
            throw new AccessDeniedException("User " + user.getUsername() + " is not associated with the company with id "
                    + id);
        }

        return (Company) abstractCustomer;
    }

    @Override
    @Transactional
    public CompanyDetailsResponseModel updateCompanyDetailsById(UUID id, @Valid CompanyUpdateRequest updateRequest, User user) {
        log.info("Updating company details for ID: {} by user: {}", id, user.getUsername());
        Company company = findCompanyToChange(id, user);
        updateCompanyFields(company, updateRequest);
        Company updatedCompany = saveCompany(company);
        log.info("Successfully updated company with ID: {}", updatedCompany.getId());

        return companyDetailsModelAssembler.toModel(updatedCompany);
    }

    /**
     * Updates the non-null fields of a company entity based on the provided request.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Updates the company type if provided in the request</li>
     * <li> Delegates the update of organization-specific details to {@code OrganizationDetailsService}</li>
     * <li>Delegates the update of customer-related details to {@code AbstractCustomerService}</li>
     * <li> Delegates the update of business entity-specific details to {@code BusinessEntityService}</li>
     * </ol>
     *
     * @param company The company entity to update
     * @param companyRequest The request containing fields to update
     */
    @Transactional
    void updateCompanyFields(Company company, @Valid CompanyUpdateRequest companyRequest) {
        log.debug("Updating fields for company ID: {}", company.getId());

        if (companyRequest.companyType() != null) {
            log.debug("Updating company type: {}", companyRequest.companyType());
            company.setCompanyType(companyRequest.companyType());
        }

        abstractCustomerService.updateAbstractCustomerDetails(company, companyRequest.customerDetails());
        businessEntityService.updateBusinessEntityFields(company, companyRequest.businessEntityDetails());

        log.info("Successfully updated fields for company ID: {}", company.getId());
    }


    @Override
    @Transactional
    public void deleteCompanyById(UUID id, User user) {
        log.info("Attempting to delete company with ID: {}", id);
        Company dbCompany = findCompanyToChange(id, user);

        log.info("Detaching company ID: {} from user associations", id);
        accountService.detachCustomerFromAccount(dbCompany);

        companyRepository.delete(dbCompany);
        log.info("Successfully deleted company with ID: {}", id);
    }

    /**
     * Locates a company that the user has permission to modify.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Retrieves the user's account and associated customer</li>
     * <li>Verifies if the user has the required permission level</li>
     * <li>Checks if the user's account is properly associated with the requested company</li>
     * </ol>
     *
     * @param id The UUID of the company to modify
     * @param user The user requesting the modification
     * @return The {@link Company} entity if found and modifiable by the user
     * @throws AccessDeniedException If the user lacks permission to modify the company
     * @throws IllegalStateException If the company is not associated with the user's account
     */
    @Transactional
    Company findCompanyToChange(UUID id, User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);

        if (accountService.isRepresentativeAssociationType(account)) {
            throw new AccessDeniedException("User " + user.getUsername() + " does not have permission to the " +
                    "company with id " + id);
        }

        if (isNotValidReferenceAbstractCustomer(id, account.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid company.");
        }
        return (Company) account.getCustomer();
    }

    /**
     * Checks if the provided customer is not a valid reference to the company with the given ID.
     * <p>
     * This method verifies that:
     * <ol>
     * <li>The customer's ID matches the provided ID</li>
     * <li>The customer is an instance of Company</li>
     * </ol>
     *
     * @param id The UUID to compare against the customer's ID
     * @param abstractCustomer The customer to validate
     * @return {@code true} if the customer is not a valid reference to the company, {@code false} otherwise
     */
    @Transactional
    boolean isNotValidReferenceAbstractCustomer(UUID id, AbstractCustomer abstractCustomer) {
        return abstractCustomer == null || !abstractCustomer.getId().equals(id) || !(abstractCustomer instanceof Company);
    }

    /**
     * Retrieves a company by its ID.
     * <p>
     * This method performs a direct database lookup for a company entity.
     *
     * @param id The UUID of the company to retrieve
     * @return The {@link Company} entity if found
     * @throws EntityNotFoundException If no company exists with the given ID
     */
    @Transactional
    Company findById(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + id + " not found"));
    }


    @Override
    @Transactional
    public CompanyDetailsResponseModel getAdminCompanyDetailsResponseModelById(UUID id) {
        log.info("Attempting to retrieve admin company details for ID: {}", id);
        Company company = findById(id);
        log.info("Retrieved admin company details: {}", company);
        CompanyDetailsResponseModel companyDetailsResponseModel = adminCompanyDetailsModelAssembler.toModel(company);
        log.info("Successfully retrieved admin company details response model: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
    }
}