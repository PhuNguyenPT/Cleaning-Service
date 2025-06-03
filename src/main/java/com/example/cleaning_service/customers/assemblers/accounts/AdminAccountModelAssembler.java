package com.example.cleaning_service.customers.assemblers.accounts;

import com.example.cleaning_service.customers.controllers.AdminCustomerController;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.mappers.AccountMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminAccountModelAssembler extends RepresentationModelAssemblerSupport<Account, AccountResponseModel> {

    private final AccountMapper accountMapper;

    public AdminAccountModelAssembler(Class<?> controllerClass, Class<AccountResponseModel> resourceType, AccountMapper accountMapper) {
        super(controllerClass, resourceType);
        this.accountMapper = accountMapper;
    }

    @Override
    protected @NonNull AccountResponseModel instantiateModel(@NonNull Account account) {
        return accountMapper.fromAccountToResponseModel(account);
    }

    @Override
    public @NonNull AccountResponseModel toModel(@NonNull Account account) {
        AccountResponseModel accountResponseModel = instantiateModel(account);
        Link selfLink = linkTo(methodOn(AdminCustomerController.class)
                .getAdminAccountDetailsResponseModelById(account.getId())).withSelfRel();
        accountResponseModel.add(selfLink);
        return accountResponseModel;
    }
}