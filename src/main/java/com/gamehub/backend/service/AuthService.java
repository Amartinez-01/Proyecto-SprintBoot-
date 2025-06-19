package com.gamehub.backend.service;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;

public interface AuthService {

    TokenResponse createUser (UserRegistrationRequest userRegistrationRequest);

    TokenResponse login (LoginRequest loginRequest);

}
