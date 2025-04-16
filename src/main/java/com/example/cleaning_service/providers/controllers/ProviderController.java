package com.example.cleaning_service.providers.controllers;

import com.example.cleaning_service.providers.assemblers.ProviderDetailsModelAssembler;
import com.example.cleaning_service.providers.assemblers.ProviderModelAssembler;
import com.example.cleaning_service.providers.dtos.ProviderDetailsModel;
import com.example.cleaning_service.providers.dtos.ProviderModel;
import com.example.cleaning_service.providers.dtos.ProviderRequest;
import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.entities.ProviderAccount;
import com.example.cleaning_service.providers.services.ProviderAccountService;
import com.example.cleaning_service.providers.services.ProviderService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/providers")
public class ProviderController {
    private final ProviderService providerService;
    private final ProviderModelAssembler providerModelAssembler;
    private final ProviderDetailsModelAssembler providerDetailsModelAssembler;
    private final ProviderAccountService providerAccountService;

    public ProviderController(ProviderService providerService, ProviderModelAssembler providerModelAssembler, ProviderDetailsModelAssembler providerDetailsModelAssembler, ProviderAccountService providerAccountService) {
        this.providerService = providerService;
        this.providerModelAssembler = providerModelAssembler;
        this.providerDetailsModelAssembler = providerDetailsModelAssembler;
        this.providerAccountService = providerAccountService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProviderModel createProvider(@RequestBody @Valid ProviderRequest providerRequest,
                                        @AuthenticationPrincipal User user) {
        Provider provider = providerService.createProvider(providerRequest, user);
        return providerModelAssembler.toModel(provider);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProviderDetailsModel getProvider(@PathVariable UUID id,
                                            @AuthenticationPrincipal User user) {
        ProviderAccount providerAccount = providerAccountService.findByProvider_IdAndUser(id, user);
        return providerDetailsModelAssembler.toModel(providerAccount.getProvider());
    }
}
