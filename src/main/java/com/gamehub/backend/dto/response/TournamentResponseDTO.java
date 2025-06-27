package com.gamehub.backend.dto.response;

import com.gamehub.backend.dto.PlayerDTO;
import com.gamehub.backend.enums.Status;
import com.gamehub.backend.model.Tournament;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class TournamentResponseDTO {
    private UUID id;
    private String name;
    private int maxPlayers;
    private Status status;
    private List<PlayerDTO> players;

    public static TournamentResponseDTO fromEntity(Tournament tournament){
        TournamentResponseDTO dto = new TournamentResponseDTO();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setMaxPlayers(tournament.getMaxPlayers());
        dto.setStatus(tournament.getStatus());
        dto.setPlayers(tournament.getPlayers().stream().map(PlayerDTO::fromEntity)
                .collect(Collectors.toList()));

        return dto;


    }

}
