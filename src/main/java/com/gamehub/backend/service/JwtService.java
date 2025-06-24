package com.gamehub.backend.service;

import com.gamehub.backend.dto.security.TokenResponse;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {

    TokenResponse generateToken (UUID userId, String role);
    Claims getClaims (String token);
    boolean isExpired(String token);
    UUID extractUserId(String token);
    String extractRole(String token);

}
