package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class GovernmentMapper {
    public Government fromGovernmentRequestToGovernment(@NotNull @Valid GovernmentRequest governmentRequest) {
        return new Government(
                governmentRequest.taxId(),
                governmentRequest.registrationNumber(),
                governmentRequest.contractorName(),
                governmentRequest.departmentName(),
                governmentRequest.isTaxExempt(),
                governmentRequest.requiresEmergencyCleaning(),
                governmentRequest.billingAddress(),
                governmentRequest.paymentMethod(),
                governmentRequest.preferredDays(),
                governmentRequest.governmentName(),
                governmentRequest.address(),
                governmentRequest.phone(),
                governmentRequest.email(),
                governmentRequest.city(),
                governmentRequest.state(),
                governmentRequest.zip(),
                governmentRequest.country(),
                governmentRequest.notes()
        );
    }

    public GovernmentResponseModel fromGovernmentToGovernmentResponseModel(@NotNull Government government) {
        return new GovernmentResponseModel(
                government.getId(),
                government.getName()
        );
    }

    public GovernmentDetailsResponseModel fromGovernmentToGovernmentDetailsResponseModel(@NotNull Government government) {
        return new GovernmentDetailsResponseModel(
                government.getTaxId(),
                government.getRegistrationNumber(),
                government.getContractorName(),
                government.getDepartmentName(),
                government.isTaxExempt(),
                government.isRequiresEmergencyCleaning(),
                government.getBillingAddress(),
                government.getPaymentMethod(),
                government.getPreferredDays(),
                government.getName(),
                government.getAddress(),
                government.getPhone(),
                government.getEmail(),
                government.getCity(),
                government.getState(),
                government.getZip(),
                government.getCountry(),
                government.getNotes()
        );
    }
}
