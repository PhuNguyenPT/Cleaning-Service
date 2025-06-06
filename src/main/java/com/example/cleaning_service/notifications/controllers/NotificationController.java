package com.example.cleaning_service.notifications.controllers;

import com.example.cleaning_service.notifications.assemblers.NotificationDetailsModelAssembler;
import com.example.cleaning_service.notifications.assemblers.NotificationModelAssembler;
import com.example.cleaning_service.notifications.dtos.NotificationDetailsModel;
import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.dtos.NotificationModel;
import com.example.cleaning_service.notifications.services.NotificationPersistenceService;
import com.example.cleaning_service.security.entities.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationPersistenceService notificationService;
    private final @Qualifier("notificationModelAssembler") NotificationModelAssembler notificationModelAssembler;
    private final @Qualifier("pagedResourcesAssemblerNotification") PagedResourcesAssembler<NotificationEntity> pagedResourcesAssemblerNotification;
    private final @Qualifier("notificationDetailsModelAssembler")NotificationDetailsModelAssembler notificationDetailsModelAssembler;

    public NotificationController(NotificationPersistenceService notificationService, NotificationModelAssembler notificationModelAssembler,
                                  PagedResourcesAssembler<NotificationEntity> pagedResourcesAssemblerNotification, NotificationDetailsModelAssembler notificationDetailsModelAssembler) {
        this.notificationService = notificationService;
        this.notificationModelAssembler = notificationModelAssembler;
        this.pagedResourcesAssemblerNotification = pagedResourcesAssemblerNotification;
        this.notificationDetailsModelAssembler = notificationDetailsModelAssembler;
    }

    @Operation(summary = "Get unread notifications", description = "Returns all unread notifications for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<NotificationModel> getUnreadNotifications(@ParameterObject Pageable pageable, @AuthenticationPrincipal User user) {
        log.info("Retrieving unread notifications for user: {}", user.getUsername());
        Page<NotificationEntity> notificationEntityPage = notificationService.getUnreadNotificationsForUser(pageable, user);
        log.info("Retrieved unread notifications for user: {} with number of elements: {}, total elements: {}",
                user.getUsername(), notificationEntityPage.getNumberOfElements(),notificationEntityPage.getTotalElements());
        pagedResourcesAssemblerNotification.setForceFirstAndLastRels(true);
        PagedModel<NotificationModel> notificationModels = pagedResourcesAssemblerNotification.toModel(
                notificationEntityPage, notificationModelAssembler
        );
        log.info("Assembling notifications to model page: {}", notificationModels);
        return notificationModels;
    }

    @Operation(summary = "Get notifications", description = "Returns notifications for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDetailsModel getNotification(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        NotificationEntity notificationEntity = notificationService.findByIdAndUser(id, user);
        NotificationDetailsModel notificationDetailsModel = notificationDetailsModelAssembler.toModel(notificationEntity);
        log.info("Assembling notifications to model: {}", notificationDetailsModel);
        return notificationDetailsModel;
    }

    @Operation(summary = "Mark notification as read", description = "Marks a notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        log.info("Marking notification {} as read for user: {}", id, user.getUsername());
        notificationService.markAsRead(id);
    }
}
