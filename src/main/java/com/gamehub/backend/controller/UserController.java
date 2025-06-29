package com.gamehub.backend.controller;

import com.gamehub.backend.dto.UserDTO;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    //  /api/users/me
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("X-User-Id");

        return userRepository.findById(userId)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .rank(user.getRank())
                .wins(user.getWins())
                .losses(user.getLosses())
                .points(user.getPoints())
                .build();
    }
}
