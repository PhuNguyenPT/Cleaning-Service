package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.api.IOrganization;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "non_profit_org")
public class NonProfitOrg extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.NON_PROFIT;

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

    public NonProfitOrg() {
    }

    public NonProfitOrg(String taxId, String registrationNumber, ECountryType countryType) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.countryType = countryType;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setCountryType(ECountryType countryType) {
        this.countryType = countryType;
    }

    @Override
    public String getTaxId() {
        return this.taxId;
    }

    @Override
    public String getRegistrationNumber() {
        return this.registrationNumber;
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