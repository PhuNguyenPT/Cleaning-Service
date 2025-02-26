package com.example.cleaning_service.security.users;

import com.example.cleaning_service.security.roles.EPermission;
import com.example.cleaning_service.security.roles.ERole;

import java.util.Set;

public record UserRequest(String username,
                          String password,
                          ERole role,
                          Set<EPermission> permissions) {
}
