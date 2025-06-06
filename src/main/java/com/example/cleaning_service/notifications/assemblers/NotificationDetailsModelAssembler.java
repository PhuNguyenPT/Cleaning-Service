package com.example.cleaning_service.notifications.assemblers;

import com.example.cleaning_service.notifications.dtos.NotificationDetailsModel;
import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.mappers.NotificationMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NotificationDetailsModelAssembler extends RepresentationModelAssemblerSupport<NotificationEntity, NotificationDetailsModel> {

    private final NotificationMapper notificationMapper;

    public NotificationDetailsModelAssembler(Class<?> controllerClass, Class<NotificationDetailsModel> resourceType, NotificationMapper notificationMapper) {
        super(controllerClass, resourceType);
        this.notificationMapper = notificationMapper;
    }

    @Override
    protected @NonNull NotificationDetailsModel instantiateModel(@NonNull NotificationEntity notificationEntity) {
        return notificationMapper.toNotificationDetailsModel(notificationEntity);
    }

    @Override
    public @NonNull NotificationDetailsModel toModel(@NonNull NotificationEntity notificationEntity) {
        return createModelWithId(notificationEntity.getId(), notificationEntity);
    }
}