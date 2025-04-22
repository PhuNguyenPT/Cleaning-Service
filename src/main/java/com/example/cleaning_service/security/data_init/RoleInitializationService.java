package com.example.cleaning_service.security.data_init;

import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleInitializationService {
    private final IRoleService roleService;

    public RoleInitializationService(IRoleService roleService) {
        this.roleService = roleService;
    }

    // Method to create an admin user (optional, you can call this when needed)
    @Transactional
    public User createAdminUser(String email, String password, PasswordEncoder passwordEncoder) {
        Role adminRole = roleService.ensureRoleExists(ERole.ADMIN);
        Set<Permission> adminPermissions = new HashSet<>(adminRole.getPermissions());
        return new User(email, passwordEncoder.encode(password), adminRole, adminPermissions);
    }
}