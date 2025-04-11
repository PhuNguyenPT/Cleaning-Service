package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

public interface GovernmentService {
    /**
     * Creates a new government entity and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Validates request to prevent duplicate fields (tax ID, registration number, email)</li>
     *   <li>Retrieves the user's current account</li>
     *   <li>Creates and persists a new government entity based on the provided request data</li>
     *   <li>Determines the appropriate association type and primary status</li>
     *   <li>Updates the user's account association with the newly created government</li>
     *   <li>Ensures that the updated account association references a valid government</li>
     * </ol>
     *
     * @param governmentRequest The request containing government entity details
     * @param user The user to associate with the government entity
     * @return A {@link GovernmentResponseModel} containing details of the created government entity
     * @throws IllegalStateException If validation fails or account association doesn't reference a valid government
     */
    GovernmentResponseModel createGovernment(GovernmentRequest governmentRequest, User user);

    /**
     * Retrieves detailed government entity information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Verifies the government entity exists using the provided ID</li>
     *   <li>Confirms the user is associated with the government entity</li>
     *   <li>Converts the government entity into a detailed response model</li>
     * </ol>
     *
     * @param id The UUID of the government entity to retrieve
     * @param user The user requesting the government entity details
     * @return A {@link GovernmentDetailsResponseModel} containing government entity information
     * @throws AccessDeniedException If the user doesn't have access to the government entity
     * @throws IllegalStateException If the government entity is not found
     */
    GovernmentDetailsResponseModel getGovernmentDetailsResponseModelById(UUID id, User user);

    /**
     * Updates government details based on the provided request.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Retrieves the government entity by its ID while ensuring the requesting user has access</li>
     *   <li>Updates only the non-null fields in the government entity based on the provided update request</li>
     *   <li>Persists the updated government entity to the database</li>
     *   <li>Converts the updated entity into a response model</li>
     * </ol>
     *
     * @param id The UUID of the government entity to update
     * @param updateRequest The request containing fields to update
     * @param user The user requesting the update
     * @return A {@link GovernmentDetailsResponseModel} containing the updated government information
     * @throws AccessDeniedException If the user doesn't have permission to update the government
     * @throws IllegalStateException If the government entity is not found or account doesn't reference a valid government
     */
    GovernmentDetailsResponseModel updateCompanyDetailsById(UUID id, GovernmentUpdateRequest updateRequest, User user);

    /**
     * Deletes a government entity by its ID and ensures it is associated with the specified user.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Verifies the government entity exists and user has deletion rights</li>
     *   <li>Detaches the government entity from any account associations linked to the user</li>
     *   <li>Deletes the government entity from the database</li>
     * </ol>
     *
     * @param id The unique identifier of the government entity to be deleted
     * @param user The user requesting the deletion
     * @throws AccessDeniedException If the user doesn't have permission to delete the government
     * @throws IllegalStateException If the government entity is not found or not properly associated
     */
    void deleteGovernmentById(UUID id, User user);

    /**
     * Administrator-only method to retrieve government details without user association checks.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Retrieves the government entity using the provided ID</li>
     *   <li>Converts the government entity into an admin-specific detailed response model</li>
     * </ol>
     *
     * @param id The UUID of the government entity to retrieve
     * @return A {@link GovernmentDetailsResponseModel} containing government entity information
     * @throws EntityNotFoundException If the government entity is not found
     */
    GovernmentDetailsResponseModel getAdminGovernmentDetailsResponseModelById(UUID id);
}