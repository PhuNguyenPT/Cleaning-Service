package com.example.cleaning_service.notifications.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Builder
@Getter
public class NotificationModel extends RepresentationModel<NotificationModel> {
    private final UUID id;
    private final String type;
    private final Long timestamp;
    private final Boolean read;
}
