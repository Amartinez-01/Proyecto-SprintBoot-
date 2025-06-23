package com.gamehub.backend.service.impl;

import com.gamehub.backend.dto.RequestTournamentDTO;
import com.gamehub.backend.dto.ResponseTournamentDTO;
import com.gamehub.backend.exception.ResourceNotFoundException;
import com.gamehub.backend.model.Tournament;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.TournamentRepository;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseTournamentDTO createTournament(RequestTournamentDTO dto) {
        if (tournamentRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un torneo con ese nombre");
        }
        Tournament tournament = Tournament.builder()
                .name(dto.getName())
                .maxPlayers(dto.getMaxPlayers())
                .status(dto.getStatus())
                .build();
        Tournament saved = tournamentRepository.save(tournament);
        return mapToDTO(saved);
    }

    @Override
    public List<ResponseTournamentDTO> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseTournamentDTO getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));
        return mapToDTO(tournament);
    }

    @Override
    public void joinTournament(UUID tournamentId, UUID userId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (tournament.getPlayers().size() >= tournament.getMaxPlayers()) {
            throw new IllegalStateException("El torneo ya esta completo");
        }

        tournament.getPlayers().add(user);
        tournamentRepository.save(tournament);
    }

    private ResponseTournamentDTO mapToDTO(Tournament t) {
        return ResponseTournamentDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .maxPlayers(t.getMaxPlayers())
                .status(t.getStatus())
                .build();
    }
}

