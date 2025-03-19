package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;

public interface IAuthService {
    AuthResponseRegisterModel register(AuthRequest authRequest);
    AuthResponseLoginModel login(AuthRequest authRequest);
    void logout(String token);
    AuthResponseProfileModel getAuthenticatedUser(User user);
}
