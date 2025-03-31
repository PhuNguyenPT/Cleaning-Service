package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.services.NonProfitOrgService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/non-profit-organizations")
@Tag(name = "User Non-profit Organizations", description = "Non-profit Organizations management APIs")
@SecurityRequirement(name = "bearerAuth")
public class NonProfitOrgController {
    private final NonProfitOrgService nonProfitOrgService;

    public NonProfitOrgController(NonProfitOrgService nonProfitOrgService) {
        this.nonProfitOrgService = nonProfitOrgService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NonProfitOrgResponseModel createProfitOrg(@RequestBody @Valid NonProfitOrgRequest nonProfitOrgRequest,
                                                     @AuthenticationPrincipal User user) {
        return nonProfitOrgService.createProfitOrg(nonProfitOrgRequest, user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NonProfitOrgDetailsResponseModel getNonProfitOrgById(@PathVariable UUID id,
                                                                @AuthenticationPrincipal User user) {
        return nonProfitOrgService.getNonProfitOrgDetailsResponseModelById(id, user);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    @ResponseStatus
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(@PathVariable UUID id,
                                                                          @RequestBody @Valid NonProfitOrgUpdateRequest updateRequest,
                                                                          @AuthenticationPrincipal User user) {
        return nonProfitOrgService.updateNonProfitOrgDetailsById(id, updateRequest, user);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNonProfitOrgById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        nonProfitOrgService.deleteNonProfitOrgById(id, user);
    }
}
