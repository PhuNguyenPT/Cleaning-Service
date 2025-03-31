package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.controllers.AccountController;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.mappers.AccountMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AccountModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountResponseModel> {

    private final AccountMapper accountMapper;
    public AccountModelAssembler(AccountMapper accountMapper) {
        super(AccountController.class, AccountResponseModel.class);
        this.accountMapper = accountMapper;
    }

    @Override
    protected @NonNull AccountResponseModel instantiateModel(@NonNull Account account) {
        return accountMapper.fromAccountToResponseModel(account);
    }

    @Override
    public @NonNull AccountResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getId(), account);
    }
}
