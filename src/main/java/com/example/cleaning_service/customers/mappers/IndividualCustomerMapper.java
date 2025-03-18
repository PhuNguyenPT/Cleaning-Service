package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component 
public class IndividualCustomerMapper {
    public IndividualCustomer fromRequestToCustomer(@NotNull IndividualCustomerRequest individualCustomerRequest) {
        return new IndividualCustomer(
                individualCustomerRequest.taxId(),
                individualCustomerRequest.registrationNumber(),
                individualCustomerRequest.billingAddress(),
                individualCustomerRequest.paymentMethod(),
                individualCustomerRequest.preferredDays(),
                individualCustomerRequest.customerName(),
                individualCustomerRequest.address(),
                individualCustomerRequest.phone(),
                individualCustomerRequest.email(),
                individualCustomerRequest.city(),
                individualCustomerRequest.state(),
                individualCustomerRequest.zip(),
                individualCustomerRequest.country(),
                individualCustomerRequest.notes()
        );
    }

    public IndividualCustomerResponseModel fromIndividualToResponseModel(@NotNull IndividualCustomer individualCustomer) {
        return new IndividualCustomerResponseModel(
                individualCustomer.getId(),
                individualCustomer.getName()
        );
    }

    public IndividualCustomerDetailsResponseModel fromCustomerToDetailsResponseModel(@NotNull IndividualCustomer individualCustomer) {
        return new IndividualCustomerDetailsResponseModel(
                individualCustomer.getTaxId(),
                individualCustomer.getRegistrationNumber(),
                individualCustomer.getBillingAddress(),
                individualCustomer.getPaymentMethod(),
                individualCustomer.getPreferredDays(),
                individualCustomer.getName(),
                individualCustomer.getAddress(),
                individualCustomer.getPhone(),
                individualCustomer.getEmail(),
                individualCustomer.getCity(),
                individualCustomer.getState(),
                individualCustomer.getZip(),
                individualCustomer.getCountry(),
                individualCustomer.getNotes()
        );
    }
}
