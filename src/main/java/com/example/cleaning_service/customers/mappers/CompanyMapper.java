package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.CompanyRequest;
import com.example.cleaning_service.customers.dto.CompanyResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company fromCompanyRequestToCompany(@NotNull CompanyRequest companyRequest) {
        return new Company(
                companyRequest.companyType(),
                companyRequest.registrationNumber(),
                companyRequest.taxId(),
                companyRequest.billingAddress(),
                companyRequest.paymentMethod(),
                companyRequest.preferredDays(),
                companyRequest.companyName(),
                companyRequest.address(),
                companyRequest.phone(),
                companyRequest.email(),
                companyRequest.city(),
                companyRequest.state(),
                companyRequest.zip(),
                companyRequest.country(),
                companyRequest.notes()
        );
    }

    public CompanyResponseModel fromCompanyToCompanyResponseModel(@NotNull Company company) {
        return new CompanyResponseModel(
                company.getId(),
                company.getName()
        );
    }

    public CompanyDetailsResponseModel fromCompanyToCompanyDetailsResponseModel(@NotNull Company company) {
        return new CompanyDetailsResponseModel(
                company.getId(),
                company.getOrganizationType(),
                company.getCompanyType(),
                company.getTaxId(),
                company.getRegistrationNumber(),
                company.getLoyaltyType(),
                company.getBillingAddress(),
                company.getPaymentMethod(),
                company.getPreferredDays(),
                company.getName(),
                company.getAddress(),
                company.getPhone(),
                company.getEmail(),
                company.getCity(),
                company.getState(),
                company.getZip(),
                company.getCountry(),
                company.getNotes()
        );
    }
}
