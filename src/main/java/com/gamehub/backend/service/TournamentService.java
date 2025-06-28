package com.gamehub.backend.service;

import com.gamehub.backend.model.Tournament;
import com.gamehub.backend.model.User;

import java.util.List;
import java.util.UUID;

public interface TournamentService {

    Tournament createTournament(Tournament tournament);
    Tournament getTournamentById(UUID id);
    List<Tournament> getAllTournaments();
    Tournament startTournament(UUID tournamentId);
    Tournament joinTournament(UUID tournamentId, User player);
    List<User> getTournamentPlayers(UUID tournamentId);
    Tournament finishTournament(UUID tournamentId);
}
