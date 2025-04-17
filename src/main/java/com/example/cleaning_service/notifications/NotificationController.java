package com.example.cleaning_service.notifications;

import com.example.cleaning_service.notifications.entites.NotificationEntity;
import com.example.cleaning_service.notifications.services.NotificationPersistenceService;
import com.example.cleaning_service.security.entities.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationPersistenceService notificationService;

    public NotificationController(NotificationPersistenceService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Get unread notifications", description = "Returns all unread notifications for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/unread")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationEntity> getUnreadNotifications(@AuthenticationPrincipal User user) {
        log.info("Retrieving unread notifications for user: {}", user.getUsername());
        return notificationService.getUnreadNotificationsForUser(user);
    }

    @Operation(summary = "Mark notification as read", description = "Marks a notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        log.info("Marking notification {} as read for user: {}", id, user.getUsername());
        notificationService.markAsRead(id);
    }
}
