package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "customer_details", schema = "customer")
public abstract class AbstractCustomer extends BusinessEntity implements ICustomer {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    protected String billingAddress;
    protected String paymentMethod;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_preferred_days", schema = "customer")
    protected Set<EDay> preferredDays = new HashSet<>();

    @Override
    public ELoyaltyType getLoyaltyType() {
        return loyaltyType;
    }

    protected void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    @Override
    public String getBillingAddress() {
        return billingAddress;
    }

    protected void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    protected void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    protected void setPreferredDays(Set<EDay> preferredDays) {
        this.preferredDays = preferredDays != null ? preferredDays : new HashSet<>();
    }
}