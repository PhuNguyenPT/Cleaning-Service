package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "governments", schema = "customer")
@ValidTaxId
@ValidRegistrationNumber
public final class Government extends AbstractCustomer implements IOrganization, ITaxIdentifiable,
        IRegistrationNumberIdentifiable
{

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final EOrganizationType organizationType = EOrganizationType.GOVERNMENT;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String taxId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String contractorName;
    private String departmentName;
    private boolean isTaxExempt;
    private boolean requiresEmergencyCleaning;

    public Government() {
    }
    public Government(String taxId, String registrationNumber, String contractorName, String departmentName,
                      boolean isTaxExempt, boolean requiresEmergencyCleaning, String billingAddress,
                      EPaymentType paymentMethod, Set<EDay> preferredDays, String name, String address, String phone,
                      String email, String city, String state, String zip, ECountryType country, String notes) {
        super(billingAddress, paymentMethod, preferredDays, name, address, phone, email, city, state, zip, country, notes);
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.contractorName = contractorName;
        this.departmentName = departmentName;
        this.isTaxExempt = isTaxExempt;
        this.requiresEmergencyCleaning = requiresEmergencyCleaning;
    }

    @Override
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
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

    public boolean isRequiresEmergencyCleaning() {
        return requiresEmergencyCleaning;
    }

    public void setRequiresEmergencyCleaning(boolean requiresEmergencyCleaning) {
        this.requiresEmergencyCleaning = requiresEmergencyCleaning;
    }
}