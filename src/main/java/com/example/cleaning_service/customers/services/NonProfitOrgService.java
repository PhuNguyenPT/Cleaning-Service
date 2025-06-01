package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

/**
 * Service interface responsible for managing Non-Profit Organization entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * non-profit organization information, ensuring data integrity and coordination with related services.
 */
public interface NonProfitOrgService {

    /**
     * Creates a new non-profit organization and associates it with the specified user.
     * <p>
     * Process steps:
     * <ol>
     *     <li>Validates the request for duplicate fields (tax ID, registration number, email)</li>
     *     <li>Checks that the user has a valid account reference</li>
     *     <li>Creates and persists a new {@link NonProfitOrg} based on the request data</li>
     *     <li>Determines appropriate association type and primary status</li>
     *     <li>Updates the user's account association with the newly created organization</li>
     * </ol>
     *
     * @param nonProfitOrgRequest The request containing non-profit organization details
     * @param user The user to associate with the non-profit organization
     * @return A {@link NonProfitOrgResponseModel} containing details of the created non-profit organization
     * @throws IllegalStateException If the organization creation process fails
     */
    NonProfitOrgResponseModel createProfitOrg(NonProfitOrgRequest nonProfitOrgRequest, User user);

    /**
     * Retrieves detailed non-profit organization information by ID for a specific user.
     * <p>
     * Process steps:
     * <ol>
     *     <li>Retrieves the {@link NonProfitOrg} entity using the provided ID</li>
     *     <li>Verifies if the user is associated with the non-profit organization</li>
     *     <li>Converts the entity into a detailed response model</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to retrieve
     * @param user The user requesting the non-profit organization details
     * @return A {@link NonProfitOrgDetailsResponseModel} containing non-profit organization information
     * @throws AccessDeniedException If the user doesn't have access to the organization
     */
    NonProfitOrgDetailsResponseModel getNonProfitOrgDetailsResponseModelById(UUID id, User user);

    /**
     * Updates non-profit organization details based on the provided request.
     * <p>
     * Process steps:
     * <ol>
     *     <li>Retrieves the {@link NonProfitOrg} entity while ensuring the user has access</li>
     *     <li>Updates the organization's fields based on the provided request</li>
     *     <li>Persists the updated entity to the database</li>
     *     <li>Converts the updated entity into a response model</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to update
     * @param updateRequest The request containing fields to update
     * @param user The user requesting the update
     * @return A {@link NonProfitOrgDetailsResponseModel} containing updated organization information
     * @throws AccessDeniedException If the user doesn't have access to modify the organization
     */
    NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(UUID id, NonProfitOrgUpdateRequest updateRequest, User user);

    /**
     * Deletes a non-profit organization by its ID after verifying user association.
     * <p>
     * Process steps:
     * <ol>
     *     <li>Retrieves the {@link NonProfitOrg} and verifies user association</li>
     *     <li>Detaches the organization from account associations</li>
     *     <li>Removes the organization from the database</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to delete
     * @param user The user requesting the deletion
     * @throws AccessDeniedException If the user doesn't have permission to delete the organization
     */
    void deleteNonProfitOrgById(UUID id, User user);

    /**
     * Retrieves detailed non-profit organization information by ID for administrative purposes.
     * <p>
     * Process steps:
     * <ol>
     *     <li>Retrieves the {@link NonProfitOrg} entity using the provided ID</li>
     *     <li>Converts the entity into an administrative response model</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to retrieve
     * @return A {@link NonProfitOrgDetailsResponseModel} containing administrative organization information
     * @throws EntityNotFoundException If the organization is not found
     */
    NonProfitOrg findById(UUID id);
}