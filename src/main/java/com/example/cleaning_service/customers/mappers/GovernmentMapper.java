package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.entities.Government;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class GovernmentMapper {
    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    public GovernmentMapper(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    public Government fromGovernmentRequestToGovernment(@Valid GovernmentRequest governmentRequest) {
        return new Government(
                governmentRequest.taxId(),
                governmentRequest.registrationNumber(),
                governmentRequest.contractorName(),
                governmentRequest.departmentName(),
                governmentRequest.isTaxExempt(),
                governmentRequest.requiresEmergencyCleaning(),
                governmentRequest.billingAddress(),
                governmentRequest.paymentMethod(),
                customerPreferredDayMapper.fromEDaysToCustomerPreferredDays(governmentRequest.preferredDays()),
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

    public GovernmentResponseModel fromGovernmentToGovernmentResponseModel(Government government) {
        return new GovernmentResponseModel(
                government.getId(),
                government.getName()
        );
    }

    public GovernmentDetailsResponseModel fromGovernmentToGovernmentDetailsResponseModel(Government government) {
        return new GovernmentDetailsResponseModel(
                government.getId(),
                government.getTaxId(),
                government.getRegistrationNumber(),
                government.getContractorName(),
                government.getDepartmentName(),
                government.isTaxExempt(),
                government.isRequiresEmergencyCleaning(),
                government.getBillingAddress(),
                government.getPaymentMethod(),
                customerPreferredDayMapper.fromCustomerPreferredDaysToEDays(government.getPreferredDays()),
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
