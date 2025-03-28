package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "non_profit_org", schema = "customer")
@ValidTaxId
@ValidRegistrationNumber
public non-sealed class NonProfitOrg extends AbstractCustomer implements IOrganization, ITaxIdentifiable,
        IRegistrationNumberIdentifiable
{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.NON_PROFIT;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    public NonProfitOrg() {
    }

    public NonProfitOrg(String taxId, String registrationNumber, String billingAddress, EPaymentType paymentMethod,
                        Set<CustomerPreferredDay> preferredDays, String name, String address, String phone, String email, String city,
                        String state, String zip, ECountryType country, String notes) {
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

    @Override
    public String registrationNumber() {
        return this.registrationNumber;
    }

    @Override
    public String taxId() {
        return this.taxId;
    }

    @Override
    public ECountryType country() {
        return this.getCountry();
    }

    @Override
    public String toString() {
        return "NonProfitOrg{" +
                "organizationType=" + organizationType +
                ", taxId='" + taxId + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                super.toString() +
                '}';
    }
}