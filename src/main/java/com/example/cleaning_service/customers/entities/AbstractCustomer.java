package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.api.ICustomer;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractCustomer extends BusinessEntity implements ICustomer {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    protected String billingAddress;
    protected String paymentMethod;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_preferred_days")
    protected Set<EDay> preferredDays = new HashSet<>();

    @Override
    public ELoyaltyType getLoyaltyType() {
        return loyaltyType;
    }

    public void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    @Override
    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(Set<EDay> preferredDays) {
        this.preferredDays = preferredDays != null ? preferredDays : new HashSet<>();
    }
}