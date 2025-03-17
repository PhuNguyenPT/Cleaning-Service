package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.enums.EPaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "individual_customers", schema = "customer")
public final class IndividualCustomer extends AbstractCustomer implements IOrganization {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;

    @Column(unique = true)
    private String taxId;

    @Column(unique = true)
    private String registrationNumber;

    public IndividualCustomer() {
    }

    public IndividualCustomer(String taxId, String registrationNumber, String billingAddress,
                              EPaymentType paymentMethod, Set<EDay> preferredDays, String name, String address,
                              String phone, String email, String city, String state, String zip, ECountryType country,
                              String notes) {
        super(billingAddress, paymentMethod, preferredDays, name, address, phone, email, city, state, zip, country, notes);
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
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