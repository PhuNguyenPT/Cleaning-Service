package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDetailsResponseModel fromAccountToDetailsResponseModel(@NotNull Account account) {
        return new AccountDetailsResponseModel(
                account.getNotes() != null ? account.getNotes() : null,
                account.isPrimary(),
                account.getAssociationType() != null ? account.getAssociationType() : null
        );
    }

    public AccountResponseModel fromAccountToResponseModel(@NotNull Account account) {
        return new AccountResponseModel(
                account.getUser() != null ? account.getUser().getUsername() : null,
                account.getCustomer() != null ? account.getCustomer().getEmail() : null,
                account.getCustomer() != null ? account.getCustomer().getName() : null
        );
    }
}
