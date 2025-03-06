package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EOrganizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "individual_customers")
public class IndividualCustomer extends AbstractCustomer {

    private final @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;

    @Column(unique = true)
    private String taxId;

    @Column(unique = true)
    private String registrationNumber;

    private Boolean isActive = true;

    // Added validation annotations
    @Override
    @Email(message = "Please provide a valid email address")
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
            message = "Phone number must be in a valid format")
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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