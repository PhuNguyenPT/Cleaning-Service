package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.ECompanyType;
import com.example.cleaning_service.customers.ELoyaltyType;
import com.example.cleaning_service.customers.EOrganizationType;
import com.example.cleaning_service.customers.ICustomer;
import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company extends BusinessEntity implements ICustomer {

    private final @Enumerated(EnumType.STRING) @Column(nullable = false) EOrganizationType organizationType = EOrganizationType.COMPANY;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ECompanyType companyType;

    private String registrationNumber;
    private String taxId;

    public Company() {
    }

    public Company(ECompanyType companyType, String registrationNumber, String taxId) {
        this.companyType = companyType;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }

    public void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    public ECompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(ECompanyType companyType) {
        this.companyType = companyType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

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
    public ELoyaltyType getLoyaltyType() {
        return this.loyaltyType;
    }
}
