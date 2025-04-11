package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

public interface CompanyService {
    /**
     * Creates a new company and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Validates the company request by checking for duplicate fields (tax ID, registration number, email)</li>
     * <li>Retrieves the user's current account and verifies its eligibility</li>
     * <li>Creates and persists a new company based on the provided request data</li>
     * <li>Determines the appropriate association type and primary status for the company</li>
     * <li>Updates the user's account with the newly created company</li>
     * <li>Ensures that the updated account references the valid company</li>
     * </ol>
     *
     * @param companyRequest The request containing company details
     * @param user The user to associate with the company
     * @return A {@link CompanyResponseModel} containing details of the created company
     * @throws IllegalStateException If the updated account does not reference a valid company
     */
    CompanyResponseModel createCompany(CompanyRequest companyRequest, User user);

    /**
     * Retrieves detailed company information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Verifies if the user is associated with the requested company</li>
     * <li>Retrieves the company entity using the provided ID</li>
     * <li>Converts the company entity into a detailed response model</li>
     * </ol>
     *
     * @param id The UUID of the company to retrieve
     * @param user The user requesting the company details
     * @return A {@link CompanyDetailsResponseModel} containing company information
     * @throws AccessDeniedException If the user is not associated with the requested company
     * @throws EntityNotFoundException If the company does not exist
     */
    CompanyDetailsResponseModel getCompanyDetailsResponseModelById(UUID id, User user);

    /**
     * Updates company details based on the provided request.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Verifies if the user has permission to update the company</li>
     * <li>Retrieves the company entity by its ID</li>
     * <li>Updates the company's fields based on the provided update request</li>
     * <li>Persists the updated company entity to the database</li>
     * <li>Converts the updated entity into a response model</li>
     * </ol>
     *
     * @param id The UUID of the company to update
     * @param updateRequest The request containing fields to update
     * @param user The user requesting the update
     * @return A {@link CompanyDetailsResponseModel} containing the updated company information
     * @throws AccessDeniedException If the user lacks permission to update the company
     * @throws IllegalStateException If the company is not associated with the user's account
     */
    CompanyDetailsResponseModel updateCompanyDetailsById(UUID id, CompanyUpdateRequest updateRequest, User user);

    /**
     * Deletes a company by its ID after verifying user permissions.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Verifies if the user has permission to delete the company</li>
     * <li>Retrieves the company entity by its ID</li>
     * <li>Detaches the company from all associated accounts</li>
     * <li>Removes the company from the database</li>
     * </ol>
     *
     * @param id The UUID of the company to delete
     * @param user The user requesting the deletion
     * @throws AccessDeniedException If the user lacks permission to delete the company
     * @throws IllegalStateException If the company is not associated with the user's account
     */
    void deleteCompanyById(UUID id, User user);

    /**
     * Retrieves detailed company information by ID for administrative purposes.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Retrieves the company entity using the provided ID</li>
     * <li>Converts the company entity into an administrative detailed response model</li>
     * <li>Returns the admin-specific view of company details</li>
     * </ol>
     * <p>
     * Note: This method does not perform user permission checks as it's intended for admin use.
     *
     * @param id The UUID of the company to retrieve
     * @return A {@link CompanyDetailsResponseModel} containing company information for admin view
     * @throws EntityNotFoundException If no company exists with the given ID
     */
    CompanyDetailsResponseModel getAdminCompanyDetailsResponseModelById(UUID id);
}
