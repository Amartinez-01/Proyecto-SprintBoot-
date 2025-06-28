package com.gamehub.backend.dto.response;

import com.gamehub.backend.dto.PlayerDTO;
import com.gamehub.backend.enums.Result;
import com.gamehub.backend.model.Match;
import lombok.Data;

import java.util.UUID;

@Data
public class MatchResponseDTO {

    private UUID id;

    private UUID tournamentId;

    private PlayerDTO player1;

    private PlayerDTO player2;

    private int round;

    private Result result;



    public static MatchResponseDTO fromEntity(Match match){
    MatchResponseDTO dto = new MatchResponseDTO();
    dto.setId(match.getId());
    dto.setTournamentId(match.getTournament().getId());
    dto.setPlayer1(PlayerDTO.fromEntity(match.getPlayer1()));
    dto.setPlayer2(PlayerDTO.fromEntity(match.getPlayer2()));
    dto.setRound(match.getRound());
    dto.setResult(match.getResult());
    return dto;

}

}
