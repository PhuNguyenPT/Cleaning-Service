package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface IndividualCustomerService {
     IndividualCustomerResponseModel createIndividualCustomer(IndividualCustomerRequest individualCustomerRequest, User user);
     IndividualCustomerDetailsResponseModel getIndividualCustomerDetailsById(UUID id, User user);
     IndividualCustomerDetailsResponseModel updateIndividualCustomerDetailsById(UUID id, IndividualCustomerUpdateRequest updateRequest, User user);
     void deleteIndividualCustomerById(UUID id, User user);
     IndividualCustomerDetailsResponseModel getAdminIndividualCustomerDetailsResponseModelById(UUID id);
}