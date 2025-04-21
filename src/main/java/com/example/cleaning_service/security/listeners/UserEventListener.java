package com.example.cleaning_service.security.listeners;

import com.example.cleaning_service.customers.events.CustomerCreationEvent;
import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.events.UserRoleUpdatedEvent;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.services.IUserService;
import com.example.cleaning_service.security.services.impl.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserEventListener {
    private final IRoleService roleService;
    private final IUserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserEventListener(RoleService roleService, IUserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.roleService = roleService;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleProviderCreatedEvent(ProviderCreatedEvent providerCreatedEvent) {
        Role providerRole = roleService.ensureRoleExists(ERole.PROVIDER);

        User user = userService.findById(providerCreatedEvent.user().getId());

        updateUserRoleAndPermissions(user, providerRole);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleCustomerCreationEvent(CustomerCreationEvent customerCreationEvent) {
        User user = userService.findById(customerCreationEvent.accountRequest().user().getId());

        Role customerRole = roleService.ensureRoleExists(ERole.CUSTOMER);

        updateUserRoleAndPermissions(user, customerRole);
    }

    @Transactional
    protected void updateUserRoleAndPermissions(User user, Role newRole) {
        log.info("Attempting to update new role {} ", newRole);
        user.addRole(newRole);

        log.info("Updating user role {} ", user.getRoles());
        Set<Permission> newRolePermissions = new HashSet<>(newRole.getPermissions());

        user.addPermissions(newRolePermissions);

        log.info("Updating user permissions {}", user.getPermissions());
        User savedUser = userService.saveUser(user);
        log.info("Updated user ID: {} ", savedUser.getId());

        UserRoleUpdatedEvent userRoleUpdatedEvent = new UserRoleUpdatedEvent(savedUser, false);
        applicationEventPublisher.publishEvent(userRoleUpdatedEvent);
        log.info("Published user role updated event of user ID: {}", userRoleUpdatedEvent.user().getId());
    }
}
