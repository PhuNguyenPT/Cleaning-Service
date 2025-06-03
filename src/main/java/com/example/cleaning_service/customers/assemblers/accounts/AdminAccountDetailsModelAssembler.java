package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
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
public class AdminAccountDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountDetailsResponseModel> {
    private final AccountMapper accountMapper;

    public AdminAccountDetailsModelAssembler(Class<?> controllerClass, Class<AccountDetailsResponseModel> resourceType, AccountMapper accountMapper) {
        super(controllerClass, resourceType);
        this.accountMapper = accountMapper;
    }

    @Override
    protected @NonNull AccountDetailsResponseModel instantiateModel(@NonNull Account account) {
        return accountMapper.fromAccountToDetailsResponseModel(account);
    }

    @Override
    public @NonNull AccountDetailsResponseModel toModel(@NonNull Account account) {
        AccountDetailsResponseModel accountDetailsResponseModel = instantiateModel(account);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminAccountDetailsResponseModelById(account.getId())).withSelfRel();
        accountDetailsResponseModel.add(selfLink);
        return accountDetailsResponseModel;
    }
}