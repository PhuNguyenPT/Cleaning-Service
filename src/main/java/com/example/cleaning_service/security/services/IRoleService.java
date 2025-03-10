package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;

public interface IRoleService {
    Role ensureRoleExists(ERole roleName);
}