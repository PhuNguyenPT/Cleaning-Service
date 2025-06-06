package com.example.cleaning_service.notifications.assemblers;

import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.dtos.NotificationModel;
import com.example.cleaning_service.notifications.mappers.NotificationMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NotificationModelAssembler extends RepresentationModelAssemblerSupport<NotificationEntity, NotificationModel> {

    private final NotificationMapper notificationMapper;

    public NotificationModelAssembler(Class<?> controllerClass, Class<NotificationModel> resourceType, NotificationMapper notificationMapper) {
        super(controllerClass, resourceType);
        this.notificationMapper = notificationMapper;
    }

    @Override
    protected @NonNull NotificationModel instantiateModel(@NonNull NotificationEntity notificationEntity) {
        return notificationMapper.toNotificationModel(notificationEntity);
    }

    @Override
    public @NonNull NotificationModel toModel(@NonNull NotificationEntity notificationEntity) {
        return createModelWithId(notificationEntity.getId(), notificationEntity);
    }
}
