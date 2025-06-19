package com.gamehub.backend.service.impl;

import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.service.JwtService;
import io.jsonwebtoken.Claims;

public class JwtServiceImpl implements JwtService {
    @Override
    public TokenResponse generateToken(Long userId, String role) {
        return null;
    }

    @Override
    public Claims getClaims(String token) {
        return null;
    }

    @Override
    public boolean isExpired(String token) {
        return false;
    }

    @Override
    public Long extractUserId(String token) {
        return 0L;
    }

    @Override
    public String extractRole(String token) {
        return "";
    }
}
