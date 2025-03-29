package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.controllers.AccountController;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.mappers.AccountMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminAccountDetailsResponseModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountDetailsResponseModel> {
    private final AccountMapper accountMapper;

    public AdminAccountDetailsResponseModelAssembler(AccountMapper accountMapper) {
        super(AccountController.class, AccountDetailsResponseModel.class);
        this.accountMapper = accountMapper;
    }

    @Override
    protected @NonNull AccountDetailsResponseModel instantiateModel(@NonNull Account account) {
        return accountMapper.fromAccountToDetailsResponseModel(account);
    }

    @Override
    public @NonNull AccountDetailsResponseModel toModel(@NonNull Account account) {
        AccountDetailsResponseModel model = instantiateModel(account);

        Link adminLink = linkTo(methodOn(AccountController.class).getAdminAccountDetailsById(account.getId()))
                .withSelfRel();
        // Use methodOn to create a link based on the controller method
        model.add(adminLink);

        return model;
    }
}