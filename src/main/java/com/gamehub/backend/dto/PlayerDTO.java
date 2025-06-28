package com.gamehub.backend.dto;

import com.gamehub.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private UUID id;
    private String username;
    private int points;

    public static PlayerDTO fromEntity(User user){
        PlayerDTO dto = new PlayerDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPoints(user.getPoints());
        return dto;
    }

}
