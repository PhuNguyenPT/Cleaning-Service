package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.api.IOrganization;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "governments")
public class Government extends AbstractCustomer implements IOrganization {

    private final @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EOrganizationType organizationType = EOrganizationType.GOVERNMENT;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String taxId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String contractorName;
    private String departmentName;
    private boolean isTaxExempt;
    private boolean isEmergencyServiceAvailable;

    public Government() {
    }

    public Government(String taxId, String registrationNumber) {
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

    public boolean isTaxExempt() {
        return isTaxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        isTaxExempt = taxExempt;
    }

    public boolean isEmergencyServiceAvailable() {
        return isEmergencyServiceAvailable;
    }

    public void setEmergencyServiceAvailable(boolean emergencyServiceAvailable) {
        isEmergencyServiceAvailable = emergencyServiceAvailable;
    }
}