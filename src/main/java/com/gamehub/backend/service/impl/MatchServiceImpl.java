package com.gamehub.backend.service.impl;

import com.gamehub.backend.enums.Result;
import com.gamehub.backend.model.Match;
import com.gamehub.backend.model.Tournament;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.MatchRepository;
import com.gamehub.backend.repository.TournamentRepository;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public MatchServiceImpl(TournamentRepository tournamentRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match getMatchById(UUID id) {
        return matchRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Match Not Found"));
    }

    @Override
    public List<Match> getMatchesByTournament(UUID tournamentId) {
        return matchRepository.findByTournamentIdOrderByRoundAsc(tournamentId);
    }

    @Override
    public List<Match> generateMatchesForRound(UUID tournamentId, int round) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Torneo No Encontrado"));
        List<User> players = (round == 1) ?
                tournament.getPlayers() :
                getWinnersFromPreviousRound(tournamentId, round - 1);

        Collections.shuffle(players);
        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < players.size(); i += 2) {
            if (i + 1 >= players.size()) break;

            Match match = new Match();
            match.setTournament(tournament);
            match.setPlayer1(players.get(i));
            match.setPlayer2(players.get(i + 1));
            match.setRound(round);
            match.setResult(Result.PENDING);
            matches.add(matchRepository.save(match));
        }

        return matches;
    }

    @Override
    public Match updateMatchResult(UUID matchId, Result result) {
        Match match = getMatchById(matchId);
        match.setResult(result);
        updatePlayerStats(match);
        return matchRepository.save(match);
    }

    @Override
    public List<Match> getMatchesByTournamentAndRound(UUID tournamentId, int round) {
        return matchRepository.findByTournamentIdAndRound(tournamentId, round);
    }

        private List<User> getWinnersFromPreviousRound(UUID tournamentId, int round) {
            return matchRepository.findByTournamentIdAndRound(tournamentId, round).stream()
                    .map(match -> {
                        if (match.getResult() == Result.PLAYER1_WIN) return match.getPlayer1();
                        else if (match.getResult() == Result.PLAYER2_WIN) return match.getPlayer2();
                        else throw new IllegalStateException("Unresolved match in previous round");
                    })
                    .collect(Collectors.toList());
        }


        private void updatePlayerStats(Match match) {
            User player1 = userRepository.findById(match.getPlayer1().getId()).get();
            User player2 = userRepository.findById(match.getPlayer2().getId()).get();

            if (match.getResult() == Result.PLAYER1_WIN) {
                player1.setWins(player1.getWins() + 1);
                player2.setLosses(player2.getLosses() + 1);
            } else if (match.getResult() == Result.PLAYER2_WIN) {
                player2.setWins(player2.getWins() + 1);
                player1.setLosses(player1.getLosses() + 1);
            }

            userRepository.saveAll(List.of(player1, player2));
        }

}
