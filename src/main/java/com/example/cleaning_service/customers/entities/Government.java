package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.IOrganization;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.ICustomer;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "governments")
public class Government extends BusinessEntity implements ICustomer, IOrganization {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false)  EOrganizationType organizationType = EOrganizationType.GOVERNMENT;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String contractorName;
    private String departmentName;
    private boolean isTaxExempt;

    private String billingAddress;
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    Set<EDay> preferredDays = new HashSet<>();
    private boolean isEmergencyServiceAvailable;

    public Government() {
        this.loyaltyType = ELoyaltyType.STANDARD;
    }

    public Government(ELoyaltyType loyaltyType, String taxId, String registrationNumber) {
        this.loyaltyType = loyaltyType;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
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

    public boolean isTaxExempt() {
        return isTaxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        isTaxExempt = taxExempt;
    }

    @Override
    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(Set<EDay> preferredDays) {
        this.preferredDays = preferredDays;
    }

    public boolean isEmergencyServiceAvailable() {
        return isEmergencyServiceAvailable;
    }

    public void setEmergencyServiceAvailable(boolean emergencyServiceAvailable) {
        isEmergencyServiceAvailable = emergencyServiceAvailable;
    }
}