package com.gamehub.backend.controller;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
public interface AuthApi {

    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest);

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @GetMapping // Mapea a /api/auth (GET)
    @PreAuthorize("isAuthenticated()") // Requiere que el usuario esté autenticado
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId);


}
