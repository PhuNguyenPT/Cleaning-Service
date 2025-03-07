package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.api.IOrganization;
import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "companies")
public class Company extends AbstractCustomer implements IOrganization {

    private final @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EOrganizationType organizationType = EOrganizationType.COMPANY;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECompanyType companyType;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String taxId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    public Company() {
    }

    public Company(ECompanyType companyType, String registrationNumber, String taxId) {
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