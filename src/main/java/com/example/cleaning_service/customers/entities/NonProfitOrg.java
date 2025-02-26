package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.IOrganization;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.ICustomer;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "non_profit_org")
public class NonProfitOrg extends BusinessEntity implements ICustomer, IOrganization {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false) EOrganizationType organizationType = EOrganizationType.NON_PROFIT;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String billingAddress;
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private Set<EDay> preferredDays;

    public NonProfitOrg() {
        this.loyaltyType = ELoyaltyType.STANDARD;
    }

    public NonProfitOrg(ELoyaltyType loyaltyType, String taxId, String registrationNumber) {
        this.loyaltyType = loyaltyType;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
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

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        return this.preferredDays;
    }
}