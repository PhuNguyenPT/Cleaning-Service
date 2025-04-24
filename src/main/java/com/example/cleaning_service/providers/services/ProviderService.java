package com.example.cleaning_service.providers.services;

import com.example.cleaning_service.providers.dtos.ProviderRequest;
import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.providers.mappers.ProviderMapper;
import com.example.cleaning_service.providers.repositories.ProviderRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider createProvider(@Valid ProviderRequest providerRequest, User user) {
        Provider provider = providerMapper.fromProviderRequest(providerRequest);
        assert provider != null;
        log.info("Provider with ID: {} successfully created.", provider.getName());
        Provider savedProvider = saveProvider(provider);
        assert savedProvider != null;
        log.info("Provider with ID: {} successfully saved.", savedProvider.getId());
        ProviderCreatedEvent providerCreatedEvent = new ProviderCreatedEvent(savedProvider, user);
        applicationEventPublisher.publishEvent(providerCreatedEvent);
        log.info("Publish event for provider created {}", providerCreatedEvent);
        return savedProvider;
    }

    @Transactional
    public Provider findById(UUID id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider with id " + id + " not found"));
    }
}
