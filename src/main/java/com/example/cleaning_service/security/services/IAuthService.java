package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.auth.AuthResponseLoginModel;
import com.example.cleaning_service.security.dtos.auth.AuthResponseLogoutModel;

public interface IAuthService {
    AuthResponseLoginModel login(AuthRequest authRequest);
    AuthResponseLogoutModel logout(String token);
}
