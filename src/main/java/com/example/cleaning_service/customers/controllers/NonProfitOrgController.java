package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.services.NonProfitOrgService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/non-profit-orgs")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(@PathVariable UUID id,
                                                                          @RequestBody @Valid NonProfitOrgUpdateRequest updateRequest,
                                                                          @AuthenticationPrincipal User user) {
        return nonProfitOrgService.updateNonProfitOrgDetailsById(id, updateRequest, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNonProfitOrgById(@PathVariable UUID id, User user) {
        nonProfitOrgService.deleteNonProfitOrgById(id, user);
    }
}
