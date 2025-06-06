package com.example.cleaning_service.notifications.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Builder
@Getter
public class NotificationDetailsModel extends RepresentationModel<NotificationDetailsModel> {
    private final UUID id;
    private final UUID userId;
    private final String type;
    private final String message;
    private final Long timestamp;
    private final Boolean read;
}
