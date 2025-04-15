package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

/**
 * Service interface for managing individual customer entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * individual customer information, ensuring data integrity and coordinating with related services.
 *
 */
public interface IndividualCustomerService {

     /**
      * Creates a new individual customer and associates it with the specified user.
      * <p>
      * This method performs the following operations:
      * <ol>
      *   <li>Validates for duplicated fields (tax ID, registration number, email)</li>
      *   <li>Creates and persists a new individual customer based on the provided request data</li>
      *   <li>Determines the appropriate association type and primary status for the individual customer</li>
      *   <li>Updates the user's account with the newly created individual customer</li>
      * </ol>
      *
      * @param individualCustomerRequest The request containing individual customer details
      * @param user The user to associate with the individual customer
      * @return A {@link IndividualCustomerResponseModel} containing details of the created individual customer
      * @throws IllegalStateException If the updated account does not reference a valid individual customer
      */
     IndividualCustomerResponseModel createIndividualCustomer(IndividualCustomerRequest individualCustomerRequest, User user);

     /**
      * Retrieves detailed individual customer information by ID for a specific user.
      * <p>
      * This method performs the following operations:
      * <ol>
      *   <li>Retrieves the individual customer entity using the provided ID</li>
      *   <li>Verifies if the user is associated with the individual customer</li>
      *   <li>Converts the individual customer entity into a detailed response model</li>
      * </ol>
      *
      * @param id The UUID of the individual customer to retrieve
      * @param user The user requesting the customer details
      * @return A {@link IndividualCustomerDetailsResponseModel} containing individual customer information
      * @throws AccessDeniedException If the user does not have access to the requested individual customer
      */
     IndividualCustomerDetailsResponseModel getIndividualCustomerDetailsById(UUID id, User user);

     /**
      * Updates individual customer details based on the provided request.
      * <p>
      * This method performs the following operations:
      * <ol>
      *   <li>Retrieves the individual customer entity by its ID while ensuring the requesting user has access</li>
      *   <li>Updates the customer's fields based on the provided update request</li>
      *   <li>Persists the updated customer entity to the database</li>
      *   <li>Converts the updated entity into a response model and returns it</li>
      * </ol>
      *
      * @param id The UUID of the individual customer to update
      * @param updateRequest The request containing fields to update
      * @param user The user requesting the update
      * @return A {@link IndividualCustomerDetailsResponseModel} containing the updated individual customer information
      * @throws AccessDeniedException If the user does not have appropriate permissions
      * @throws IllegalStateException If the individual customer is not found or the user doesn't have access
      */
     IndividualCustomerDetailsResponseModel updateIndividualCustomerDetailsById(UUID id, IndividualCustomerUpdateRequest updateRequest, User user);

     /**
      * Deletes an individual customer by its ID and ensures it is associated with the specified user.
      * <p>
      * This method performs the following operations:
      * <ol>
      *   <li>Retrieves the individual customer by its ID and verifies that it is associated with the given user</li>
      *   <li>Detaches the individual customer from any account linked to the user</li>
      *   <li>Deletes the individual customer from the database</li>
      * </ol>
      *
      * @param id The unique identifier of the individual customer to be deleted
      * @param user The user requesting the deletion
      * @throws AccessDeniedException If the user does not have appropriate permissions
      * @throws IllegalStateException If the account does not reference a valid individual customer
      */
     void deleteIndividualCustomerById(UUID id, User user);

     /**
      * Retrieves detailed individual customer information by ID for administrative purposes.
      * <p>
      * This method is intended for administrative use and bypasses the normal user association checks,
      * allowing administrators to access any individual customer's details.
      *
      * @param id The UUID of the individual customer to retrieve
      * @return A {@link IndividualCustomerDetailsResponseModel} containing individual customer information
      * @throws EntityNotFoundException If the individual customer with the specified ID is not found
      */
     IndividualCustomerDetailsResponseModel getAdminIndividualCustomerDetailsResponseModelById(UUID id);
}