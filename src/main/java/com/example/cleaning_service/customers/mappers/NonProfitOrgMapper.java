package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NonProfitOrgMapper {

    public NonProfitOrg fromRequestToNonProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest) {
        return new NonProfitOrg(
                nonProfitOrgRequest.taxId(),
                nonProfitOrgRequest.registrationNumber(),
                nonProfitOrgRequest.billingAddress(),
                nonProfitOrgRequest.paymentMethod(),
                nonProfitOrgRequest.preferredDays(),
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

    public NonProfitOrgResponseModel fromNonProfitOrgToModel(@NotNull NonProfitOrg nonProfitOrg) {
        return new NonProfitOrgResponseModel(
                nonProfitOrg.getId(),
                nonProfitOrg.getName()
        );
    }

    public NonProfitOrgDetailsResponseModel fromNonProfitOrgToDetailsModel(@NotNull NonProfitOrg nonProfitOrg) {
        return new NonProfitOrgDetailsResponseModel(
                nonProfitOrg.getTaxId(),
                nonProfitOrg.getRegistrationNumber(),
                nonProfitOrg.getBillingAddress(),
                nonProfitOrg.getPaymentMethod(),
                nonProfitOrg.getPreferredDays(),
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
