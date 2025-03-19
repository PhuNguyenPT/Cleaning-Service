package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.controllers.AccountController;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.mappers.AccountMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountDetailsResponseModel> {
    private final AccountMapper accountMapper;

    public AccountDetailsResponseModelAssembler(AccountMapper accountMapper) {
        super(AccountController.class, AccountDetailsResponseModel.class);
        this.accountMapper = accountMapper;
    }

    @Override
    protected @NonNull AccountDetailsResponseModel instantiateModel(@NonNull Account account) {
        return accountMapper.fromAccountToDetailsResponseModel(account);
    }

    @Override
    public @NonNull AccountDetailsResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getId(), account);
    }
}
