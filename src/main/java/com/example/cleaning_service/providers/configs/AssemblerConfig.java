package com.example.cleaning_service.providers.configs;

import com.example.cleaning_service.providers.assemblers.ProviderDetailsModelAssembler;
import com.example.cleaning_service.providers.assemblers.ProviderModelAssembler;
import com.example.cleaning_service.providers.controllers.ProviderController;
import com.example.cleaning_service.providers.dtos.ProviderDetailsModel;
import com.example.cleaning_service.providers.dtos.ProviderModel;
import com.example.cleaning_service.providers.mappers.ProviderMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssemblerConfig {
    @Bean
    public ProviderDetailsModelAssembler providerDetailsModelAssembler(ProviderMapper providerMapper) {
        return new ProviderDetailsModelAssembler(ProviderController.class, ProviderDetailsModel.class, providerMapper);
    }

    @Bean
    public ProviderModelAssembler providerModelAssembler(ProviderMapper providerMapper) {
        return new ProviderModelAssembler(ProviderController.class, ProviderModel.class, providerMapper);
    }
}
