package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface CompanyService {
    CompanyResponseModel createCompany(CompanyRequest companyRequest, User user);
    CompanyDetailsResponseModel getCompanyDetailsResponseModelById(UUID id, User user);
    CompanyDetailsResponseModel updateCompanyDetailsById(UUID id, CompanyUpdateRequest updateRequest, User user);
    void deleteCompanyById(UUID id, User user);
    CompanyDetailsResponseModel getAdminCompanyDetailsResponseModelById(UUID id);
}
