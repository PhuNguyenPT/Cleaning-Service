package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.commons.BusinessEntity;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "customer_details", schema = "customer")
public abstract class AbstractCustomer extends BusinessEntity implements ICustomer, ITaxIdentifiable,
        IRegistrationNumberIdentifiable
{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    protected String taxId;

    protected String registrationNumber;

    protected String billingAddress;

    @Enumerated(EnumType.STRING)
    protected EPaymentType paymentMethod;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    protected Set<CustomerPreferredDay> preferredDays = new HashSet<>();

    public AbstractCustomer() {
        super();
    }

    public AbstractCustomer(String taxId, String registrationNumber, String billingAddress, EPaymentType paymentMethod, Set<CustomerPreferredDay> preferredDays,
                            String name, String address, String phone, String email, String city, String state,
                            String zip, ECountryType country, String notes) {

        super(name, address, phone, email, city, state, zip, country, notes);

        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays != null ? new HashSet<>(preferredDays) : new HashSet<>();
    }

    @Override
    public ELoyaltyType getLoyaltyType() {
        return loyaltyType;
    }

    @Override
    public String getTaxId() {
        return taxId;
    }

    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String taxId() {
        return this.taxId;
    }

    @Override
    public String registrationNumber() {
        return this.registrationNumber;
    }

    @Override
    public ECountryType country() {
        return this.country;
    }

    @Override
    public String getBillingAddress() {
        return billingAddress;
    }

    @Override
    public EPaymentType getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public Set<CustomerPreferredDay> getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(Set<CustomerPreferredDay> preferredDays) {
        this.preferredDays = preferredDays != null ? preferredDays : new HashSet<>();
    }

    @Override
    public String toString() {
        return "AbstractCustomer{" +
                "loyaltyType=" + loyaltyType +
                ", taxId='" + taxId + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", preferredDays=" + preferredDays +
                super.toString() +
                '}';
    }
}