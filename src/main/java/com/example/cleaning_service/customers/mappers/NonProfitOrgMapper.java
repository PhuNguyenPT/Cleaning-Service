package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class NonProfitOrgMapper {

    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    public NonProfitOrgMapper(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    public NonProfitOrg fromRequestToNonProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest) {
        return new NonProfitOrg(
                nonProfitOrgRequest.taxId(),
                nonProfitOrgRequest.registrationNumber(),
                nonProfitOrgRequest.billingAddress(),
                nonProfitOrgRequest.paymentMethod(),
                customerPreferredDayMapper.fromEDaysToCustomerPreferredDays(nonProfitOrgRequest.preferredDays()),
                nonProfitOrgRequest.organizationName(),
                nonProfitOrgRequest.address(),
                nonProfitOrgRequest.phone(),
                nonProfitOrgRequest.email(),
                nonProfitOrgRequest.city(),
                nonProfitOrgRequest.state(),
                nonProfitOrgRequest.zip(),
                nonProfitOrgRequest.country(),
                nonProfitOrgRequest.notes()
        );
    }

    public NonProfitOrgResponseModel fromNonProfitOrgToModel(NonProfitOrg nonProfitOrg) {
        return new NonProfitOrgResponseModel(
                nonProfitOrg.getId(),
                nonProfitOrg.getName()
        );
    }

    public NonProfitOrgDetailsResponseModel fromNonProfitOrgToDetailsModel(NonProfitOrg nonProfitOrg) {
        return new NonProfitOrgDetailsResponseModel(
                nonProfitOrg.getId(),
                nonProfitOrg.getTaxId(),
                nonProfitOrg.getRegistrationNumber(),
                nonProfitOrg.getBillingAddress(),
                nonProfitOrg.getPaymentMethod(),
                customerPreferredDayMapper.fromCustomerPreferredDaysToEDays(nonProfitOrg.getPreferredDays()),
                nonProfitOrg.getName(),
                nonProfitOrg.getAddress(),
                nonProfitOrg.getPhone(),
                nonProfitOrg.getEmail(),
                nonProfitOrg.getCity(),
                nonProfitOrg.getState(),
                nonProfitOrg.getZip(),
                nonProfitOrg.getCountry(),
                nonProfitOrg.getNotes()
        );
    }
}
