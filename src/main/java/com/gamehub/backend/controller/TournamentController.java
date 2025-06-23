package com.gamehub.backend.controller;

import com.gamehub.backend.dto.RequestTournamentDTO;
import com.gamehub.backend.dto.ResponseTournamentDTO;
import com.gamehub.backend.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseTournamentDTO> createTournament(@Valid @RequestBody RequestTournamentDTO dto) {
        return ResponseEntity.ok(tournamentService.createTournament(dto));
    }

    @GetMapping
    public ResponseEntity<List<ResponseTournamentDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTournamentDTO> getTournament(@PathVariable UUID id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinTournament(@PathVariable UUID id, Principal principal) {
        System.out.println("PRINCIPAL: " + principal.getName());
        UUID userId = UUID.fromString(principal.getName());
        tournamentService.joinTournament(id, userId);
        return ResponseEntity.ok("Unido correctamente al torneo");
    }

}

