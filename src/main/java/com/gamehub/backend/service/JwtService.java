package com.gamehub.backend.service;

import com.gamehub.backend.dto.security.TokenResponse;
import io.jsonwebtoken.Claims;

public interface JwtService {

    TokenResponse generateToken (Long userId, String role);
    Claims getClaims (String token);
    boolean isExpired(String token);
    Long extractUserId(String token);
    String extractRole(String token);

}
