package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.enums.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.UUID;

public class CompanyDetailsResponseModel extends RepresentationModel<CompanyDetailsResponseModel> {
    private final UUID id;
    private final EOrganizationType organizationType;
    private final ECompanyType companyType;
    private final String taxId;
    private final String registrationNumber;

    private final ELoyaltyType loyaltyType;
    private final String billingAddress;
    private final EPaymentType paymentMethod;
    private final Set<EDay> preferredDays;

    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final String city;
    private final String state;
    private final String zip;
    private final ECountryType country;
    private final String notes;

    public CompanyDetailsResponseModel(UUID id, EOrganizationType organizationType, ECompanyType companyType, String taxId, String registrationNumber, ELoyaltyType loyaltyType, String billingAddress, EPaymentType paymentMethod, Set<EDay> preferredDays, String name, String address, String phone, String email, String city, String state, String zip, ECountryType country, String notes) {
        this.id = id;
        this.organizationType = organizationType;
        this.companyType = companyType;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.loyaltyType = loyaltyType;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.notes = notes;
    }
}
