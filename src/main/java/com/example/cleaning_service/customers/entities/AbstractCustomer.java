package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.commons.BusinessEntity;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EPaymentType;
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

    @Enumerated(EnumType.STRING)
    protected EPaymentType paymentMethod;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_preferred_days", schema = "customer",
            joinColumns = @JoinColumn(name = "customer_id"))
    protected Set<EDay> preferredDays = new HashSet<>();

    public AbstractCustomer() {
        super();
    }

    public AbstractCustomer(String billingAddress, EPaymentType paymentMethod, Set<EDay> preferredDays,
                            String name, String address, String phone, String email, String city, String state,
                            String zip, ECountryType country, String notes) {

        super(name, address, phone, email, city, state, zip, country, notes);

        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays != null ? new HashSet<>(preferredDays) : new HashSet<>();
    }

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

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public EPaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EPaymentType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(Set<EDay> preferredDays) {
        this.preferredDays = preferredDays != null ? preferredDays : new HashSet<>();
    }

    @Override
    public String toString() {
        return "AbstractCustomer{" +
                "loyaltyType=" + loyaltyType +
                ", billingAddress='" + billingAddress + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", preferredDays=" + preferredDays +
                super.toString() +
                '}';
    }
}