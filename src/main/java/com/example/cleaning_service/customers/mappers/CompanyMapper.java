package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.entities.Company;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    public CompanyMapper(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    public Company fromCompanyRequestToCompany(@Valid CompanyRequest companyRequest) {
        return new Company(
                companyRequest.companyType(),
                companyRequest.registrationNumber(),
                companyRequest.taxId(),
                companyRequest.billingAddress(),
                companyRequest.paymentMethod(),
                customerPreferredDayMapper.fromEDaysToCustomerPreferredDays(companyRequest.preferredDays()),
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

    public CompanyResponseModel fromCompanyToCompanyResponseModel(Company company) {
        return new CompanyResponseModel(
                company.getId(),
                company.getName()
        );
    }

    public CompanyDetailsResponseModel fromCompanyToCompanyDetailsResponseModel(Company company) {
        return new CompanyDetailsResponseModel(
                company.getId(),
                company.getOrganizationType(),
                company.getCompanyType(),
                company.getTaxId(),
                company.getRegistrationNumber(),
                company.getLoyaltyType(),
                company.getBillingAddress(),
                company.getPaymentMethod(),
                customerPreferredDayMapper.fromCustomerPreferredDaysToEDays(company.getPreferredDays()),
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
