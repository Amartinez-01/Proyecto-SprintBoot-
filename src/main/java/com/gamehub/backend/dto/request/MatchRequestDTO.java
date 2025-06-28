package com.gamehub.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class MatchRequestDTO {
    @NotNull
    private UUID tournamentId;

    @NotNull
    private UUID player1Id;

    @NotNull
    private UUID player2Id;

    @Min(1)
    private int round;

}
