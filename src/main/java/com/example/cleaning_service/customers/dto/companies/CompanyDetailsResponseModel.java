package com.example.cleaning_service.customers.dto.companies;

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

    public CompanyDetailsResponseModel(UUID id, EOrganizationType organizationType, ECompanyType companyType,
                                       String taxId, String registrationNumber, ELoyaltyType loyaltyType,
                                       String billingAddress, EPaymentType paymentMethod,
                                       Set<EDay> preferredDays, String name, String address,
                                       String phone, String email, String city, String state, String zip,
                                       ECountryType country, String notes) {
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

    public UUID getId() {
        return id;
    }

    public EOrganizationType getOrganizationType() {
        return organizationType;
    }

    public ECompanyType getCompanyType() {
        return companyType;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public ELoyaltyType getLoyaltyType() {
        return loyaltyType;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public EPaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public ECountryType getCountry() {
        return country;
    }

    public String getNotes() {
        return notes;
    }
}
