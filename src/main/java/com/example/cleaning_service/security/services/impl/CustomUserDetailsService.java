package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findWithRolesAndPermissionsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
