package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/me", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseModel getAccountByUser(@AuthenticationPrincipal User user) {
        return accountService.getAccountResponseModelById(user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAccountDetailsById(@PathVariable UUID id,
                                                             @AuthenticationPrincipal User user) {
        return accountService.getAccountDetailsResponseModelById(id, user);
    }
}
