package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgModelAssembler;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.services.NonProfitOrgService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/non-profit-organizations")
@Tag(name = "User Non-profit Organizations", description = "Non-profit Organizations management APIs")
@SecurityRequirement(name = "bearerAuth")
public class NonProfitOrgController {
    private final NonProfitOrgService nonProfitOrgService;
    private final NonProfitOrgModelAssembler nonProfitOrgModelAssembler;
    private final NonProfitOrgDetailsModelAssembler nonProfitOrgDetailsModelAssembler;

    public NonProfitOrgController(NonProfitOrgService nonProfitOrgService, NonProfitOrgModelAssembler nonProfitOrgModelAssembler, NonProfitOrgDetailsModelAssembler nonProfitOrgDetailsModelAssembler) {
        this.nonProfitOrgService = nonProfitOrgService;
        this.nonProfitOrgModelAssembler = nonProfitOrgModelAssembler;
        this.nonProfitOrgDetailsModelAssembler = nonProfitOrgDetailsModelAssembler;
    }

    @Operation(summary = "Create a Non-profit Organization", description = "Creates a new non-profit organization associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Non-profit organization created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NonProfitOrgResponseModel createNonProfitOrg(@RequestBody @Valid NonProfitOrgRequest nonProfitOrgRequest,
                                                        @AuthenticationPrincipal User user) {
        NonProfitOrg nonProfitOrg = nonProfitOrgService.createProfitOrg(nonProfitOrgRequest, user);
        NonProfitOrgResponseModel nonProfitOrgResponseModel = nonProfitOrgModelAssembler.toModel(nonProfitOrg);
        log.info("Successfully created non-profit response: {}", nonProfitOrgResponseModel);

        return nonProfitOrgResponseModel;
    }

    @Operation(summary = "Get Non-profit Organization by ID", description = "Retrieves a non-profit organization by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Non-profit organization retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Non-profit organization not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NonProfitOrgDetailsResponseModel getNonProfitOrgById(@PathVariable UUID id,
                                                                @AuthenticationPrincipal User user) {
        NonProfitOrg nonProfitOrg = nonProfitOrgService.getNonProfitOrgDetailsResponseModelById(id, user);
        return nonProfitOrgDetailsModelAssembler.toModel(nonProfitOrg);
    }

    @Operation(summary = "Update Non-profit Organization", description = "Updates a non-profit organization's details. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Non-profit organization updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this non-profit organization"),
            @ApiResponse(responseCode = "404", description = "Non-profit organization not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(@PathVariable UUID id,
                                                                          @RequestBody @Valid NonProfitOrgUpdateRequest updateRequest,
                                                                          @AuthenticationPrincipal User user) {
        NonProfitOrg nonProfitOrg = nonProfitOrgService.updateNonProfitOrgDetailsById(id, updateRequest, user);
        return nonProfitOrgDetailsModelAssembler.toModel(nonProfitOrg);

    }

    @Operation(summary = "Delete Non-profit Organization", description = "Deletes a non-profit organization by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Non-profit organization deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this non-profit organization"),
            @ApiResponse(responseCode = "404", description = "Non-profit organization not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNonProfitOrgById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        nonProfitOrgService.deleteNonProfitOrgById(id, user);
    }
}