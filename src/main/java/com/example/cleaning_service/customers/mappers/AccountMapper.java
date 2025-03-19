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
                account.getNotes(),
                account.isPrimary(),
                account.getAssociationType()
        );
    }

    public AccountResponseModel fromAccountToResponseModel(@NotNull Account account) {
        return new AccountResponseModel(
                account.getUser().getUsername(),
                account.getCustomer().getEmail(),
                account.getCustomer().getName()
        );
    }
}
