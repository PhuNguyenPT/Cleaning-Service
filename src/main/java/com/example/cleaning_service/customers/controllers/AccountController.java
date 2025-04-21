package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/accounts")
@Tag(name = "User Accounts", description = "Account management APIs")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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
        return accountService.getAccountResponseModelById(user);
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
        return accountService.getAccountDetailsResponseModelById(id, user);
    }
}