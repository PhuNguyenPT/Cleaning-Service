package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.ICustomer;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "individual_customers")
public class IndividualCustomer extends BusinessEntity implements ICustomer {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false) EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType;

    @Column(unique = true)
    private String taxId;

    @Column(unique = true)
    private String registrationNumber;

    private Boolean isActive;

    private String billingAddress;
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private Set<EDay> preferredDays = new HashSet<>();
    public IndividualCustomer() {
        this.isActive = true;
        this.loyaltyType = ELoyaltyType.STANDARD;
    }

    public IndividualCustomer(String taxId, String registrationNumber) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.isActive = true;
        this.loyaltyType = ELoyaltyType.STANDARD;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
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

    @Override
    public ELoyaltyType getLoyaltyType() {
        return this.loyaltyType;
    }

    @Override
    public String getBillingAddress() {
        return this.billingAddress;
    }

    @Override
    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    @Override
    public Set<EDay> getPreferredDays() {
        return Set.of();
    }
}