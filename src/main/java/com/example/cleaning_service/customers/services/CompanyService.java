package com.example.cleaning_service.customers.services;

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
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for managing Company entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * company information, ensuring data integrity and coordinating with related services.
 */
@Service
public class CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
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

    public CompanyService(
            CompanyRepository companyRepository,

            AccountService accountService,
            BusinessEntityService businessEntityService,
            AbstractCustomerService abstractCustomerService,
            OrganizationDetailsService organizationDetailsService,
            CustomerService customerService,

            CompanyModelAssembler companyModelAssembler,
            CompanyDetailsModelAssembler companyDetailsModelAssembler,

            CompanyMapper companyMapper,
            AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler) {

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
    }

    /**
     * Creates a new company and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the user's current account.
     * 2. Creates and persists a new company based on the provided request data.
     * 3. Determines the appropriate association type and primary status for the company.
     * 4. Updates the user's account with the newly created company.
     * 5. Ensures that the updated account references a valid company.
     *
     * @param companyRequest The request containing company details.
     * @param user The user to associate with the company.
     * @return A {@link CompanyResponseModel} containing details of the created company.
     * @throws IllegalStateException If the updated account does not reference a valid company.
     */
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
        Account account = accountService.findAccountWithCustomerByUser(user);
        accountService.checkAccountReferenceCustomer(account);

        // Save the company
        Company company = companyMapper.fromCompanyRequestToCompany(companyRequest);
        Company savedCompany = saveCompany(company);
        log.info("Company {} saved successfully with ID: {}", savedCompany.getName(), savedCompany.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedCompany);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedCompany);

        // Update account
        AccountRequest accountRequest = new AccountRequest(
                savedCompany, null, isPrimary, associationType
        );
        Account updatedAccount = accountService.updateAccount(accountRequest, account);

        // Ensure the account correctly references a company
        if (isNotValidReferenceAbstractCustomer(updatedAccount.getCustomer().getId(), updatedAccount.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid company.");
        }

        CompanyResponseModel companyResponseModel = companyModelAssembler.toModel((Company) updatedAccount.getCustomer());
        log.info("Successfully created company response: {}", companyResponseModel);

        return companyResponseModel;
    }

    /**
     * Saves a company entity to the database.
     *
     * @param company The company entity to persist.
     * @return The saved company entity with updated metadata.
     */
    @Transactional
    protected Company saveCompany(Company company) {
        log.info("Saving company: {}", company);
        Company savedCompany = companyRepository.save(company);
        log.info("Company saved with ID: {}", savedCompany.getId());
        return savedCompany;
    }

    /**
     * Retrieves detailed company information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the company entity using the provided ID.
     * 2. Verifies if the user is associated with the company.
     * 3. Converts the company entity into a detailed response model.
     * @param id The UUID of the company to retrieve
     * @param user The user requesting the company details
     * @return A detailed response model containing company information
     * @throws IllegalStateException If the company is not found or the user doesn't have access
     */
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
     * 1. Retrieves the company entity using the provided ID.
     * 2. Check if the user is associated with the company through an account.
     * 3. If the user lacks the required association, throw an {@code IllegalStateException}.
     *
     * @param id The UUID of the company to retrieve.
     * @param user The user requesting access to the company.
     * @return The company entity if found and accessible by the user.
     * @throws AccessDeniedException If the company is not found or if the user does not have
     *                               the required association.
     */
    @Transactional
    protected Company getByIdAndUser(UUID id, User user) {
        log.info("Fetching company with ID: {} for user: {}", id, user.getUsername());
        AbstractCustomer abstractCustomer = accountService.findAccountWithCustomerByUser(user).getCustomer();
        if (isNotValidReferenceAbstractCustomer(id, abstractCustomer)) {
            throw new AccessDeniedException("User " + user.getUsername() + " is not associated with the company with id "
                    + id);
        }

        return (Company) abstractCustomer;
    }

    /**
     * Updates company details based on the provided request.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the company entity by its ID while ensuring the requesting user has access.
     * 2. Updates the company's fields based on the provided update request.
     * 3. Persists the updated company entity to the database.
     * 4. Converts the updated entity into a response model and returns it.
     * @param id The UUID of the company to update
     * @param updateRequest The request containing fields to update
     * @param user The user requesting the update
     * @return A detailed response model containing the updated company information
     * @throws IllegalStateException If the company is not found or user doesn't have access
     */
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
     * Updates only the non-null fields of the company entity.
     * <p>
     * This method performs the following operations:
     * 1. Checks if the company type is provided in the request and updates it if present.
     * 2. Delegates the update of organization-specific details to {@code OrganizationDetailsService}.
     * 3. Delegates the update of customer-related details to {@code AbstractCustomerService}.
     * 4. Delegates the update of business entity-specific details to {@code BusinessEntityService}.
     *
     * @param company The company entity to update.
     * @param companyRequest The request containing fields to update.
     */
    @Transactional
    protected void updateCompanyFields(Company company, @Valid CompanyUpdateRequest companyRequest) {
        log.debug("Updating fields for company ID: {}", company.getId());

        if (companyRequest.companyType() != null) {
            log.debug("Updating company type: {}", companyRequest.companyType());
            company.setCompanyType(companyRequest.companyType());
        }

        organizationDetailsService.updateOrganizationDetails(company, companyRequest.organizationDetails());
        abstractCustomerService.updateAbstractCustomerDetails(company, companyRequest.customerDetails());
        businessEntityService.updateBusinessEntityFields(company, companyRequest.businessEntityDetails());

        log.info("Successfully updated fields for company ID: {}", company.getId());
    }

    @Transactional
    public void deleteCompanyById(UUID id, User user) {
        log.info("Attempting to delete company with ID: {}", id);
        Company dbCompany = findCompanyToChange(id, user);

        log.info("Detaching company ID: {} from user associations", id);
        accountService.detachCustomerFromAccount(dbCompany);

        companyRepository.delete(dbCompany);
        log.info("Successfully deleted company with ID: {}", id);
    }

    @Transactional
    protected Company findCompanyToChange(UUID id, User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);

        if (accountService.isRepresentativeAssociationType(account)) {
            throw new AccessDeniedException("User " + user.getUsername() + " does not have permission to update the " +
                    "company with id " + id);
        }

        if (isNotValidReferenceAbstractCustomer(id, account.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid company.");
        }
        return (Company) account.getCustomer();
    }

    @Transactional
    protected boolean isNotValidReferenceAbstractCustomer(UUID id, AbstractCustomer abstractCustomer) {
        return !abstractCustomer.getId().equals(id) || !(abstractCustomer instanceof Company);
    }

    @Transactional
    Company findById(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + id + " not found"));
    }

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
