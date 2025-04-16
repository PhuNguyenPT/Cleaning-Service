package com.example.cleaning_service.security.entities.role;

import com.example.cleaning_service.security.entities.permission.EPermission;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(enumAsRef = true, example = "USER")
public enum ERole {
    ADMIN(Set.of(EPermission.MANAGE_USERS, EPermission.MANAGE_ROLES, EPermission.VIEW_REPORTS, EPermission.DELETE_ACCOUNTS)),
    USER(Set.of(EPermission.CREATE_ACCOUNT)),
    CUSTOMER(Set.of(EPermission.CREATE_ORDERS, EPermission.CANCEL_ORDERS, EPermission.VIEW_ORDERS, EPermission.RATE_CLEANERS)),
    CLEANER(Set.of(EPermission.VIEW_ASSIGNED_ORDERS, EPermission.UPDATE_ORDER_STATUS)),
    SUPERVISOR(Set.of(EPermission.ASSIGN_CLEANERS, EPermission.MONITOR_CLEANING_PROGRESS)),
    SUPPORT(Set.of(EPermission.RESPOND_TO_QUERIES, EPermission.VIEW_ORDERS, EPermission.MANAGE_SCHEDULES)),
    PROVIDER(Set.of(EPermission.VIEW_ORDERS, EPermission.UPDATE_ORDER_STATUS));

    private final Set<EPermission> permissions;

    ERole(Set<EPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<EPermission> getPermissions() {
        return permissions;
    }
}