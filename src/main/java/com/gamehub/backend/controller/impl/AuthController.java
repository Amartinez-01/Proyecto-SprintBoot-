package com.gamehub.backend.controller.impl;

import com.gamehub.backend.controller.AuthApi;
import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import com.gamehub.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> createUser(UserRegistrationRequest userRegistrationRequest) {
        TokenResponse response = authService.createUser(userRegistrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> getUser(@RequestAttribute (name = "X-User-Id") String userId) {
        return ResponseEntity.ok(userId);
    }
}
