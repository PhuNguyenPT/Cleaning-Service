package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.*;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "companies", schema = "customer")
@ValidTaxId
@ValidRegistrationNumber
public non-sealed class Company extends AbstractCustomer implements IOrganization
{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.COMPANY;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECompanyType companyType;

    public Company() {
        super();
    }

    public Company(ECompanyType companyType, String taxId, String registrationNumber,
                   String billingAddress, EPaymentType paymentMethod, Set<CustomerPreferredDay> preferredDays,
                   String name, String address, String phone, String email, String city, String state,
                   String zip, ECountryType country, String notes) {

        super(taxId, registrationNumber, billingAddress, paymentMethod, preferredDays, name, address, phone, email, city, state, zip, country, notes);

        this.companyType = companyType;
    }

    public ECompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(ECompanyType companyType) {
        this.companyType = companyType;
    }

    @Override
    public EOrganizationType getOrganizationType() {
        return this.organizationType;
    }

    @Override
    public ECountryType country() {
        return this.getCountry();
    }

    @Override
    public String toString() {
        return "Company{" +
                "organizationType=" + organizationType +
                ", companyType=" + companyType +
                super.toString() +
                '}';
    }
}