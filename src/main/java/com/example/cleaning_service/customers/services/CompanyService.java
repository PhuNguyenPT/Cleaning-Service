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
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final AccountAssociationService accountAssociationService;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;
    private final CompanyMapper companyMapper;
    private final CompanyResponseModelAssembler companyResponseModelAssembler;
    private final CompanyDetailsResponseModelAssembler companyDetailsResponseModelAssembler;

    /**
     * Constructs a CompanyService with required dependencies.
     *
     * @param companyRepository Repository for company persistence operations.
     * @param accountAssociationService Service for managing user-company associations.
     * @param businessEntityService Service for managing business entity operations.
     * @param abstractCustomerService Service for managing customer-related operations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param companyMapper Mapper for converting between DTOs and entities.
     * @param companyResponseModelAssembler Assembler for basic company response models.
     * @param companyDetailsResponseModelAssembler Assembler for detailed company response models.
     */
    public CompanyService(
            CompanyRepository companyRepository,
            AccountAssociationService accountAssociationService,
            BusinessEntityService businessEntityService,
            AbstractCustomerService abstractCustomerService,
            OrganizationDetailsService organizationDetailsService,
            CompanyMapper companyMapper,
            CompanyResponseModelAssembler companyResponseModelAssembler,
            CompanyDetailsResponseModelAssembler companyDetailsResponseModelAssembler) {

        this.companyRepository = companyRepository;
        this.accountAssociationService = accountAssociationService;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
        this.companyMapper = companyMapper;
        this.companyResponseModelAssembler = companyResponseModelAssembler;
        this.companyDetailsResponseModelAssembler = companyDetailsResponseModelAssembler;
    }

    /**
     * Creates a new company and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the user's current account association.
     * 2. Creates and persists a new company based on the provided request data.
     * 3. Determines the appropriate association type and primary status for the company.
     * 4. Updates the user's account association with the newly created company.
     * 5. Ensures that the updated account association references a valid company.
     *
     * @param companyRequest The request containing company details.
     * @param user The user to associate with the company.
     * @return A {@link CompanyResponseModel} containing details of the created company.
     * @throws IllegalStateException If the updated account association does not reference a valid company.
     */
    @Transactional
    public CompanyResponseModel createCompany(@NotNull CompanyRequest companyRequest, @NotNull User user) {
        log.info("Attempting to create a company for user: {}", user.getUsername());
        AccountAssociation accountAssociation = accountAssociationService.findByUser(user);

        // Save the company
        Company company = companyMapper.fromCompanyRequestToCompany(companyRequest);
        Company savedCompany = saveCompany(company);
        log.info("Company {} saved successfully with ID: {}", savedCompany.getName(), savedCompany.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedCompany);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedCompany);

        // Update account association
        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(
                savedCompany, null, isPrimary, associationType
        );
        AccountAssociation updatedAccountAssociation = accountAssociationService.updateAccountAssociation(accountAssociationRequest,accountAssociation);

        // Ensure the account association correctly references a company
        if (!(updatedAccountAssociation.getCustomer() instanceof Company accountCompany)) {
            throw new IllegalStateException("Account association does not reference a valid company.");
        }

        CompanyResponseModel companyResponseModel = companyResponseModelAssembler.toModel(accountCompany);
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
    Company saveCompany(@NotNull Company company) {
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
        CompanyDetailsResponseModel companyDetailsResponseModel = companyDetailsResponseModelAssembler.toModel(dbCompany);
        log.info("Retrieved company details: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
    }

    /**
     * Retrieves a company by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the company entity using the provided ID.
     * 2. Check if the user is associated with the company through an account association.
     * 3. If the user lacks the required association, throw an {@code IllegalStateException}.
     *
     * @param id The UUID of the company to retrieve.
     * @param user The user requesting access to the company.
     * @return The company entity if found and accessible by the user.
     * @throws IllegalStateException If the company is not found or if the user does not have
     *                               the required association.
     */
    @Transactional
    Company getByIdAndUser(UUID id, User user) {
        log.info("Fetching company with ID: {} for user: {}", id, user.getUsername());
        Company company = findById(id);
        if (accountAssociationService.isNotExistsAccountAssociationByUserAndCustomer(user, company)) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with the company.");
        }
        return company;
    }

    /**
     * Finds a company by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Attempts to retrieve the company entity from the database using the provided ID.
     * 2. If the company exists, it is returned.
     * 3. If no company is found, an {@code IllegalStateException} is thrown.
     * @param id The UUID of the company to find
     * @return The company entity if found
     * @throws EntityNotFoundException If no company exists with the given ID
     */
    @Transactional
    Company findById(UUID id) {
        log.info("Looking for company with ID: {}", id);
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + id + " not found."));
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
        Company dbCompany = getByIdAndUser(id, user);

        updateCompanyFields(dbCompany, updateRequest);
        Company updatedCompany = saveCompany(dbCompany);

        log.info("Successfully updated company with ID: {}", updatedCompany.getId());
        return companyDetailsResponseModelAssembler.toModel(updatedCompany);
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
    void updateCompanyFields(Company company, @Valid CompanyUpdateRequest companyRequest) {
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
    public void deleteCompanyById(UUID id) {
        log.info("Attempting to delete company with ID: {}", id);
        Company dbCompany = findById(id);

        log.info("Detaching company ID: {} from user associations", id);
        accountAssociationService.detachCustomerFromAssociation(dbCompany);

        companyRepository.delete(dbCompany);
        log.info("Successfully deleted company with ID: {}", id);
    }
}
