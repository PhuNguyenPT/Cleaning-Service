package com.example.cleaning_service.providers.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class ProviderModel extends RepresentationModel<ProviderModel> {
    private final UUID id;
    private final String name;
}
