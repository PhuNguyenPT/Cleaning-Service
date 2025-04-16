package com.example.cleaning_service.security.listeners;

import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.services.IUserService;
import com.example.cleaning_service.security.services.impl.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserEventListener {
    private final IRoleService roleService;
    private final IUserService userService;

    public UserEventListener(RoleService roleService, IUserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleProviderCreatedEvent(ProviderCreatedEvent providerCreatedEvent) {
        Role providerRole = roleService.ensureRoleExists(ERole.PROVIDER);
        log.info("Provider role {} created.", providerRole.getName());
        User user = userService.findById(providerCreatedEvent.user().getId());
        if (user == null) {
            log.error("User in ProviderCreatedEvent is null.");
            throw new IllegalStateException("User in ProviderCreatedEvent is null.");
        }
        user.addRole(providerRole);
        log.info("Set role for user ID: {} to '{}'", user.getId(), providerRole.getName());

        Set<Permission> copiedPermissions = new HashSet<>(providerRole.getPermissions());
        user.addPermissions(copiedPermissions);
        log.info("Set permissions for user ID: {} to '{}'", user.getId(), copiedPermissions);

        User savedUser = userService.saveUser(user);

        Set<String> userRoles = user.getRoles().stream().map(role -> role.getName().name())
                .collect(Collectors.toSet());
        log.info("User with ID: {} successfully updated with new role {}.", savedUser.getId(), userRoles);
    }
}
