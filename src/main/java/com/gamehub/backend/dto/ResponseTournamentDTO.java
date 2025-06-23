package com.gamehub.backend.dto;

import com.gamehub.backend.enums.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTournamentDTO {
    private UUID id;
    private String name;
    private int maxPlayers;
    private Status status;
}
