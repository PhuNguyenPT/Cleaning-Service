package com.example.cleaning_service.security.data_init;

import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return new User(email, passwordEncoder.encode(password), adminRole, adminRole.getPermissions());
    }
}