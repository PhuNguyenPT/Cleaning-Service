package com.example.cleaning_service.notifications.configs;

import com.example.cleaning_service.notifications.assemblers.NotificationDetailsModelAssembler;
import com.example.cleaning_service.notifications.assemblers.NotificationModelAssembler;
import com.example.cleaning_service.notifications.controllers.NotificationController;
import com.example.cleaning_service.notifications.dtos.NotificationDetailsModel;
import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.dtos.NotificationModel;
import com.example.cleaning_service.notifications.mappers.NotificationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
public class NotificationConfigs {
    @Bean(name = {"notificationModelAssembler"})
    NotificationModelAssembler notificationModelAssembler(NotificationMapper notificationMapper) {
        return new NotificationModelAssembler(NotificationController.class, NotificationModel.class, notificationMapper);
    }

    @Bean(name = "pagedResourcesAssemblerNotification")
    PagedResourcesAssembler<NotificationEntity> pagedResourcesAssemblerNotification(PagedResourcesAssembler<NotificationEntity> pagedResourcesAssembler) {
        return pagedResourcesAssembler;
    }

    @Bean(name = {"notificationDetailsModelAssembler"})
    NotificationDetailsModelAssembler notificationDetailsModelAssembler(NotificationMapper notificationMapper) {
        return new NotificationDetailsModelAssembler(NotificationController.class, NotificationDetailsModel.class, notificationMapper);
    }
}