package com.example.cleaning_service.customers.dto.accounts;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;

import java.util.UUID;

public class AccountResponseModel extends RepresentationModel<AccountResponseModel> {
    private final UUID id;
    private final String username;
    private final String email;
    private final String customerName;

    public AccountResponseModel(UUID id, String username, String email, String customerName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.customerName = customerName;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public @NonNull String toString() {
        return "AccountResponseModel{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
