package com.gamehub.backend.controller;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserRegistrationRequest request) {
        TokenResponse token = authService.createUser(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
