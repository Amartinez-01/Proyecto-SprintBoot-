package com.gamehub.backend.dto;

import com.gamehub.backend.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private String rank;
    private int wins;
    private int losses;
    private int points;
}
