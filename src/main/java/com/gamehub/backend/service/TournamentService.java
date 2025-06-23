package com.gamehub.backend.service;

import com.gamehub.backend.dto.RequestTournamentDTO;
import com.gamehub.backend.dto.ResponseTournamentDTO;

import java.util.List;
import java.util.UUID;

public interface TournamentService {

    ResponseTournamentDTO createTournament(RequestTournamentDTO dto);

    List<ResponseTournamentDTO> getAllTournaments();

    ResponseTournamentDTO getTournamentById(UUID id);

    void joinTournament(UUID tournamentId, UUID userId);
}

