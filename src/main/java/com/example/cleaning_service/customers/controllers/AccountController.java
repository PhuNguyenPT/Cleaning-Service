package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Accounts", description = "Account management APIs")
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/accounts/me", produces = {"application/hal+json"})
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return accountService.getAccountDetailsResponseModelById(id, user);
    }

    @Operation(
            summary = "Get all accounts (admin)",
            description = "Admin-only endpoint to retrieve a paginated list of all accounts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Accounts retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/accounts", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<AccountResponseModel> getAllAccountDetailsPageModelByPage(
            @Parameter(description = "Pagination parameters") @ParameterObject Pageable pageable
    ) {
        return accountService.getAccountDetailsPageModelByPageable(pageable);
    }

    @Operation(
            summary = "Get account details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed account information by ID",
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAdminAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id
    ) {
        return accountService.getAccountDetailsResponseModelById(id);
    }

    @Operation(
            summary = "Update account details (admin)",
            description = "Admin-only endpoint to update account information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account updated successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/admin/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel patchAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Account data to update") @RequestBody @Valid AccountUpdateRequest accountUpdateRequest
    ) {
        return accountService.patchAccountDetailsById(id, accountUpdateRequest);
    }
}