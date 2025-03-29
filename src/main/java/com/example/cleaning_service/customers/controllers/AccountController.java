package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.entities.user.User;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/accounts/me", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseModel getAccountByUser(@AuthenticationPrincipal User user) {
        return accountService.getAccountResponseModelById(user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAdminAccountDetailsById(@PathVariable UUID id,
                                                                  @AuthenticationPrincipal User user) {
        return accountService.getAccountDetailsResponseModelById(id, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/accounts", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<AccountResponseModel> getAllAccountDetailsPageModelByPage(@ParameterObject Pageable pageable) {
        return accountService.getAccountDetailsPageModelByPageable(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAdminAccountDetailsById(@PathVariable UUID id) {
        return accountService.getAccountDetailsResponseModelById(id);
    }
}
