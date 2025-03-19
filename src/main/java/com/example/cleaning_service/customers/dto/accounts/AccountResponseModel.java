package com.example.cleaning_service.customers.dto.accounts;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;

import java.util.UUID;

public class AccountResponseModel extends RepresentationModel<AccountResponseModel> {
    private final String username;
    private final String email;
    private final String customerName;

    public AccountResponseModel(String username, String email, String customerName) {
        this.username = username;
        this.email = email;
        this.customerName = customerName;
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
