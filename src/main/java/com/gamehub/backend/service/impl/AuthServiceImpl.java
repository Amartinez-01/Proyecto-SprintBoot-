package com.gamehub.backend.service.impl;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import com.gamehub.backend.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public TokenResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        return null;
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        return null;
    }
}
