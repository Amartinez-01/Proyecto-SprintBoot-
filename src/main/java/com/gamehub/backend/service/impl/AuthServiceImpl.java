package com.gamehub.backend.service.impl;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import com.gamehub.backend.enums.Role;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PLAYER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getRole().name()).getAccessToken();

        return new TokenResponse(token);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email inválido"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña inválida");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole().name()).getAccessToken();

        return new TokenResponse(token);
    }
}

