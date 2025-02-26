package com.example.cleaning_service.security.data_init;

import com.example.cleaning_service.security.users.User;
import com.example.cleaning_service.security.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final RoleInitializationService roleInitializationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(RoleInitializationService roleInitializationService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleInitializationService = roleInitializationService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create an admin user if none exists
        if (userRepository.findByUsername("admin@example.com").isEmpty()) {
            User adminUser = roleInitializationService.createAdminUser(
                    "admin@example.com",
                    "adminPassword123",
                    passwordEncoder
            );
            userRepository.save(adminUser);
        }
    }
}