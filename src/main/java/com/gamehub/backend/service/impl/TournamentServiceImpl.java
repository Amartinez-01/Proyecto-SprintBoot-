package com.gamehub.backend.service.impl;

import com.gamehub.backend.enums.Status;
import com.gamehub.backend.model.Tournament;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.TournamentRepository;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.TournamentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        tournament.setStatus(Status.CREATED);
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament getTournamentById(UUID id) {
        return tournamentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Torneo No Encontrado"));
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament startTournament(UUID tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);
        if (tournament.getPlayers().size() < 2 ){
            throw new IllegalStateException("Al menos deben haber 2 jugadores");
        }
        tournament.setStatus(Status.IN_PROGRESS);
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament joinTournament(UUID tournamentId, User player) {
        Tournament tournament = getTournamentById(tournamentId);
        if (tournament.getStatus() != Status.CREATED) {
            throw new IllegalStateException("Tournament already started");
        }
        tournament.getPlayers().add(player);
        return tournamentRepository.save(tournament);
    }

    @Override
    public List<User> getTournamentPlayers(UUID tournamentId) {
        return getTournamentById(tournamentId).getPlayers();
    }

    @Override
    public Tournament finishTournament(UUID tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);
        tournament.setStatus(Status.FINISHED);
        return tournamentRepository.save(tournament);
    }
}
