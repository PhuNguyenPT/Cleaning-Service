package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.companies.AdminCompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.AdminGovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.AdminIndividualDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.AdminNonProfitOrgDetailsModelAssembler;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/accounts/{id}/customers")
@Tag(name = "Admin Accounts", description = "Account management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminCustomerController {

    private final AccountService accountService;
    private final AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler;
    private final AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler;
    private final AdminIndividualDetailsModelAssembler adminIndividualDetailsModelAssembler;
    private final AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler;

    public AdminCustomerController(
            AccountService accountService,
            AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler,
            AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler,
            AdminIndividualDetailsModelAssembler adminIndividualDetailsModelAssembler,
            AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler
    ) {
        this.accountService = accountService;
        this.adminCompanyDetailsModelAssembler = adminCompanyDetailsModelAssembler;
        this.adminGovernmentDetailsModelAssembler = adminGovernmentDetailsModelAssembler;
        this.adminIndividualDetailsModelAssembler = adminIndividualDetailsModelAssembler;
        this.adminNonProfitOrgDetailsModelAssembler = adminNonProfitOrgDetailsModelAssembler;
    }


    @Operation(
            summary = "Get company details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed company information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Company details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{customerId}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> getAdminCustomerDetailsResponseModelById(@PathVariable UUID id, @PathVariable UUID customerId) {
        Account account = accountService.findWithCustomerById(id);
        RepresentationModel<?> representationModel = getRepresentationModel(account, customerId);
        Assert.notNull(representationModel, "Account details not found");
        return representationModel;
    }

    private RepresentationModel<?> getRepresentationModel(Account account, UUID customerId) {
        Assert.notNull(account, "Account must not be null");
        Assert.notNull(account.getCustomer(), "Customer must not be null");
        Assert.notNull(customerId, "CustomerId must not be null");
        switch (account.getCustomer()) {
            case Company c -> {
                Assert.isTrue(c.getId().equals(customerId), "Customer id does not match");
                CompanyDetailsResponseModel companyDetailsResponseModel = adminCompanyDetailsModelAssembler.toModel(account);
                log.info("Successfully retrieved admin company details response model: {}", companyDetailsResponseModel);
                return companyDetailsResponseModel;
            }
            case Government g -> {
                Assert.isTrue(g.getId().equals(customerId), "Customer id does not match");
                GovernmentDetailsResponseModel governmentDetailsResponseModel = adminGovernmentDetailsModelAssembler.toModel(account);
                log.info("Successfully retrieved admin government details response model: {}", governmentDetailsResponseModel);
                return governmentDetailsResponseModel;
            }
            case IndividualCustomer ic -> {
                Assert.isTrue(ic.getId().equals(customerId), "Customer id does not match");
                IndividualCustomerDetailsResponseModel individualCustomerDetailsResponseModel = adminIndividualDetailsModelAssembler.toModel(account);
                log.info("Successfully retrieved admin individual details response model: {}", individualCustomerDetailsResponseModel);
                return individualCustomerDetailsResponseModel;
            }
            case NonProfitOrg npo -> {
                Assert.isTrue(npo.getId().equals(customerId), "Customer id does not match");
                NonProfitOrgDetailsResponseModel nonProfitOrgDetailsResponseModel = adminNonProfitOrgDetailsModelAssembler.toModel(account);
                log.info("Successfully retrieved admin non-profit org details response model: {}", nonProfitOrgDetailsResponseModel);
                return nonProfitOrgDetailsResponseModel;
            }
            default -> throw new IllegalStateException("Unexpected value: " + account.getCustomer());
        }
    }
}
