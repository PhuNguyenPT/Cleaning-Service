package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.api.IOrganization;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "individual_customers", schema = "customer")
public class IndividualCustomer extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;

    @Column(unique = true)
    @ValidTaxId
    private String taxId;

    @Column(unique = true)
    @ValidRegistrationNumber
    private String registrationNumber;

    // Added validation annotations
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    public IndividualCustomer() {
    }

    public IndividualCustomer(String taxId, String registrationNumber) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setRegistrationNumber(String registrationNumber) {
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