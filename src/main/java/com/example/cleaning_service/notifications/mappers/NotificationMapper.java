package com.example.cleaning_service.notifications.mappers;

import com.example.cleaning_service.notifications.dtos.NotificationDetailsModel;
import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.dtos.NotificationModel;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationModel toNotificationModel(NotificationEntity notificationEntity) {
        return NotificationModel.builder()
                .id(notificationEntity.getId())
                .type(notificationEntity.getType())
                .timestamp(notificationEntity.getTimestamp())
                .read(notificationEntity.isRead())
                .build();
    }

    public NotificationDetailsModel toNotificationDetailsModel(NotificationEntity notificationEntity) {
        return NotificationDetailsModel.builder()
                .id(notificationEntity.getId())
                .userId(notificationEntity.getUser().getId())
                .type(notificationEntity.getType())
                .message(notificationEntity.getMessage())
                .timestamp(notificationEntity.getTimestamp())
                .read(notificationEntity.isRead())
                .build();
    }
}
