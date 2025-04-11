package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface GovernmentService {
    GovernmentResponseModel createGovernment(GovernmentRequest governmentRequest, User user);
    GovernmentDetailsResponseModel getGovernmentDetailsResponseModelById(UUID id, User user);
    GovernmentDetailsResponseModel updateCompanyDetailsById(UUID id, GovernmentUpdateRequest updateRequest, User user);
    void deleteGovernmentById(UUID id, User user);
    GovernmentDetailsResponseModel getAdminGovernmentDetailsResponseModelById(UUID id);
}
