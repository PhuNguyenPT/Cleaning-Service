package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.entities.permission.EPermission;
import com.example.cleaning_service.security.entities.permission.Permission;

import java.util.Set;

public interface IPermissionService {
    Set<Permission> ensurePermissionsExist(Set<EPermission> requiredPermissions);
}