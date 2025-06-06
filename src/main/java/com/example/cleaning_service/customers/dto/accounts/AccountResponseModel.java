package com.example.cleaning_service.customers.dto.accounts;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Getter
public class AccountResponseModel extends RepresentationModel<AccountResponseModel> {
    private final UUID id;
    private final String username;
    private final String email;
    private final UUID customerId;
    private final String customerName;

    public AccountResponseModel(UUID id, String username, String email, UUID customerId, String customerName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.customerId = customerId;
        this.customerName = customerName;
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
