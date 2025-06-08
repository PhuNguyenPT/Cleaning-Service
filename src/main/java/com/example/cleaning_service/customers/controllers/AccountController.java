package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.accounts.AccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AccountModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.IOrganization;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.customers.services.OrganizationDetailsService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users/accounts")
@Tag(name = "User Accounts", description = "Account management APIs")
public class AccountController {
    private final AccountService accountService;
    private final AccountModelAssembler accountModelAssembler;
    private final OrganizationDetailsService organizationDetailsService;
    private final AccountDetailsModelAssembler accountDetailsModelAssembler;

    public AccountController(AccountService accountService,
                             AccountModelAssembler accountModelAssembler,
                             OrganizationDetailsService organizationDetailsService,
                             AccountDetailsModelAssembler accountDetailsModelAssembler) {
        this.accountService = accountService;
        this.accountModelAssembler = accountModelAssembler;
        this.organizationDetailsService = organizationDetailsService;
        this.accountDetailsModelAssembler = accountDetailsModelAssembler;
    }

    @Operation(
            summary = "Get current user's account (user)",
            description = "Retrieves the account details of the currently authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(path = "/me", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseModel getAccountByUser(@AuthenticationPrincipal User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);
        AccountResponseModel accountResponseModel = accountModelAssembler.toModel(account);
        log.info("Retrieved account model {}", accountResponseModel);

        return accountResponseModel;
    }

    @Operation(
            summary = "Get account details by ID (user)",
            description = "Allows regular users to retrieve specific account details by ID if authorized",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            }
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(path = "/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        Account account = accountService.getAccountDetailsResponseModelById(id, user);
        AccountDetailsResponseModel accountDetailsResponseModel = accountDetailsModelAssembler.toModel(account);
        log.info("Retrieved account details model {}", accountDetailsResponseModel);

        if (account.getCustomer() != null) {
            Link customerLink = organizationDetailsService.getLinkByIOrganization(
                    (IOrganization) account.getCustomer()
            );
            log.info("Retrieved customer link {}", customerLink);
            accountDetailsResponseModel.add(customerLink);
        }

        return accountDetailsResponseModel;
    }
}