package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.mappers.AccountMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountDetailsResponseModel> {
    private final AccountMapper accountMapper;

    public AccountDetailsModelAssembler(Class<?> controllerClass, Class<AccountDetailsResponseModel> resourceType, AccountMapper accountMapper) {
        super(controllerClass, resourceType);
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
