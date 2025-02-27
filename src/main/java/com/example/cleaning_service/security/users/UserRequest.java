package com.example.cleaning_service.security.users;

import com.example.cleaning_service.security.roles.ERole;

public record UserRequest(String username,
                          String password,
                          ERole role) {
}
