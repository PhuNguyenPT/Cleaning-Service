package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.*;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "companies", schema = "customer")
public non-sealed class Company extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.COMPANY;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECompanyType companyType;

    @NotBlank
    @Column(nullable = false, unique = true)
    @ValidTaxId
    private String taxId;

    @NotBlank
    @Column(nullable = false, unique = true)
    @ValidRegistrationNumber
    private String registrationNumber;

    public Company() {
        super();
    }

    public Company(ECompanyType companyType, String registrationNumber, String taxId,
                   String billingAddress, EPaymentType paymentMethod, Set<EDay> preferredDays,
                   String name, String address, String phone, String email, String city, String state,
                   String zip, ECountryType country, String notes) {

        super(billingAddress, paymentMethod, preferredDays, name, address, phone, email, city, state, zip, country, notes);

        this.companyType = companyType;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }

    public ECompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(ECompanyType companyType) {
        this.companyType = companyType;
    }


    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
    public EOrganizationType getOrganizationType() {
        return this.organizationType;
    }
}