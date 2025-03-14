package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "non_profit_org", schema = "customer")
public non-sealed class NonProfitOrg extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.NON_PROFIT;

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

    public NonProfitOrg(String taxId, String registrationNumber) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void setTaxId(@ValidTaxId String taxId) {
        this.taxId = taxId;
    }

    @Override
    public void setRegistrationNumber(@ValidRegistrationNumber String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
}