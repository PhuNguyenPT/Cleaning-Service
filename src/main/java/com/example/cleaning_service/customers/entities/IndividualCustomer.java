package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "individual_customers", schema = "customer")
@ValidTaxId
@ValidRegistrationNumber
public non-sealed class IndividualCustomer extends AbstractCustomer implements IOrganization
{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;

    public IndividualCustomer() {
    }

    public IndividualCustomer(String taxId, String registrationNumber, String billingAddress,
                              EPaymentType paymentMethod, Set<CustomerPreferredDay> preferredDays, String name, String address,
                              String phone, String email, String city, String state, String zip, ECountryType country,
                              String notes) {
        super(taxId, registrationNumber, billingAddress, paymentMethod, preferredDays, name, address, phone, email, city, state, zip, country, notes);
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
        return super.toString();
    }
}