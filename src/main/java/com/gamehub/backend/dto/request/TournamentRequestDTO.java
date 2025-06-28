package com.gamehub.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TournamentRequestDTO {
    @NotBlank
    private String name;

    @Min(2)
    private int maxPlayers;
}
