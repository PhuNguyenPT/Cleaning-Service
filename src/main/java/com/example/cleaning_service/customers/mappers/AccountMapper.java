package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDetailsResponseModel fromAccountToDetailsResponseModel(Account account) {
        return new AccountDetailsResponseModel(
                account.getId(),
                account.getNotes(),
                account.isPrimary(),
                account.getAssociationType()
        );
    }

    public AccountResponseModel fromAccountToResponseModel(Account account) {
        return new AccountResponseModel(
                account.getId(),
                account.getUser() != null ? account.getUser().getUsername() : null,
                account.getCustomer() != null ? account.getCustomer().getEmail() : null,
                account.getCustomer().getId() != null ? account.getCustomer().getId() : null,
                account.getCustomer() != null ? account.getCustomer().getName() : null
        );
    }
}
