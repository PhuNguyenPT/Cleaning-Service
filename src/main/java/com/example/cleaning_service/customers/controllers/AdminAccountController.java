package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.accounts.AccountModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountDetailsModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.customers.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/admin/accounts")
@Tag(name = "Admin Accounts", description = "Account management APIs")
public class AdminAccountController {
    private final AccountRepository accountRepository;
    private final PagedResourcesAssembler<Account> pagedResourcesAssembler;
    private final @Qualifier("adminAccountModelAssembler") AccountModelAssembler adminAccountModelAssembler;
    private final AccountService accountService;
    private final @Qualifier("adminAccountDetailsModelAssembler") AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler;

    public AdminAccountController(AccountRepository accountRepository,
                                  @Qualifier("pagedResourcesAssemblerAccount")
                                  PagedResourcesAssembler<Account> pagedResourcesAssembler,
                                  AccountModelAssembler adminAccountModelAssembler,
                                  AccountService accountService,
                                  AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler) {
        this.accountRepository = accountRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminAccountModelAssembler = adminAccountModelAssembler;
        this.accountService = accountService;
        this.adminAccountDetailsModelAssembler = adminAccountDetailsModelAssembler;
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
    @GetMapping(produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<AccountResponseModel> getAdminAccountDetailsPageModelByPageable(
            @Parameter(description = "Pagination parameters") @ParameterObject Pageable pageable
    ) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        pagedResourcesAssembler.setForceFirstAndLastRels(true);
        PagedModel<AccountResponseModel> accountResponseModels = pagedResourcesAssembler.toModel(
                accountPage, adminAccountModelAssembler
        );
        log.info("Retrieved account page model {}", accountResponseModels);

        return accountResponseModels;
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
    @GetMapping(path = "/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAdminAccountDetailsResponseModelById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id
    ) {
        Account account = accountService.findWithCustomerById(id);
        AccountDetailsResponseModel accountDetailsResponseModel = adminAccountDetailsModelAssembler.toModel(account);
        accountDetailsResponseModel.add(this.getCustomerLink(account));
        log.info("Retrieved admin finding: account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
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
    @PatchMapping(path = "/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel patchAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Account data to update") @RequestBody @Valid AccountUpdateRequest accountUpdateRequest
    ) {
        Account patchedAccount = accountService.patchAccountDetailsById(id, accountUpdateRequest);
        AccountDetailsResponseModel accountDetailsResponseModel = adminAccountDetailsModelAssembler.toModel(patchedAccount);
        accountDetailsResponseModel.add(this.getCustomerLink(patchedAccount));
        log.info("Patched account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
    }

    private Link getCustomerLink(Account account) {
        Assert.notNull(account, "Account must not be null");
        Assert.notNull(account.getCustomer(), "Customer must not be null");

        return getAdminCustomerLink(account.getId(), account.getCustomer().getId());
    }

    private Link getAdminCustomerLink(UUID accountId, UUID customerId) {
        Assert.notNull(accountId, "Account ID must not be null");
        Assert.notNull(customerId, "Customer ID must not be null");
        return linkTo(methodOn(AdminCustomerController.class)
                .getAdminCustomerDetailsResponseModelById(accountId, customerId))
                .withRel("customer");
    }
}
