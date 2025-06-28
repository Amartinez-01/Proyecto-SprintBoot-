package com.gamehub.backend.service;

import com.gamehub.backend.enums.Result;
import com.gamehub.backend.model.Match;

import java.util.List;
import java.util.UUID;

public interface MatchService {

    Match createMatch(Match match);
    Match getMatchById(UUID id);
    List<Match> getMatchesByTournament(UUID tournamentId);
    List<Match> generateMatchesForRound(UUID tournamentId, int round);
    Match updateMatchResult(UUID matchId, Result result);
    List<Match> getMatchesByTournamentAndRound(UUID tournamentId, int round);
}
