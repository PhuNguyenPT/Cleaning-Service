package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.api.IOrganization;
import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "companies")
public class Company extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.COMPANY;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECompanyType companyType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECountryType countryType;

    @NotBlank
    @Column(nullable = false, unique = true)
    @ValidTaxId
    private String taxId;

    @NotBlank
    @Column(nullable = false, unique = true)
    @ValidRegistrationNumber
    private String registrationNumber;

    public Company() {
    }

    public Company(ECompanyType companyType, ECountryType countryType, String registrationNumber, String taxId) {
        this.companyType = companyType;
        this.countryType = countryType;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }

    public ECompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(ECompanyType companyType) {
        this.companyType = companyType;
    }

    public void setCountryType(ECountryType countryType) {
        this.countryType = countryType;
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

    @Override
    public ECountryType getCountryType() {
        return this.countryType;
    }
}