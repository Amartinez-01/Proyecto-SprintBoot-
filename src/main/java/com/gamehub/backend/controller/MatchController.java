package com.gamehub.backend.controller;

import com.gamehub.backend.dto.response.MatchResponseDTO;
import com.gamehub.backend.enums.Result;
import com.gamehub.backend.model.Match;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Match Management", description = "Endpoints para gestión de partidas")
public class MatchController {

    private final MatchService matchService;
    private final UserRepository userRepository;

    // ========== ENDPOINTS PRINCIPALES ========== //

    @Operation(summary = "Generar partidas", description = "Crea los emparejamientos para una ronda (requiere rol ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partidas generadas"),
            @ApiResponse(responseCode = "400", description = "Error en generación")
    })
    @PostMapping("/generate/{tournamentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MatchResponseDTO>> generateMatches(
            @PathVariable UUID tournamentId,
            @RequestParam(defaultValue = "1") int round) {

        List<Match> matches = matchService.generateMatchesForRound(tournamentId, round);
        return ResponseEntity.ok(
                matches.stream()
                        .map(MatchResponseDTO::fromEntity)
                        .toList()
        );
    }

    @Operation(summary = "Obtener partida", description = "Devuelve los detalles de una partida específica")
    @ApiResponse(responseCode = "200", description = "Partida encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable UUID id) {
        Match match = matchService.getMatchById(id);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Listar partidas por torneo", description = "Devuelve todas las partidas de un torneo")
    @ApiResponse(responseCode = "200", description = "Lista de partidas")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByTournament(
            @PathVariable UUID tournamentId) {

        return ResponseEntity.ok(
                matchService.getMatchesByTournament(tournamentId).stream()
                        .map(MatchResponseDTO::fromEntity)
                        .toList()
        );
    }

    @Operation(summary = "Listar partidas por ronda", description = "Devuelve las partidas de una ronda específica")
    @ApiResponse(responseCode = "200", description = "Lista de partidas")
    @GetMapping("/tournament/{tournamentId}/round/{round}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByRound(
            @PathVariable UUID tournamentId,
            @PathVariable int round) {

        return ResponseEntity.ok(
                matchService.getMatchesByTournamentAndRound(tournamentId, round).stream()
                        .map(MatchResponseDTO::fromEntity)
                        .toList()
        );
    }

    @Operation(summary = "Actualizar resultado", description = "Actualiza el resultado de una partida (requiere rol ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado actualizado"),
            @ApiResponse(responseCode = "400", description = "Resultado inválido")
    })
    @PutMapping("/{id}/result")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchResponseDTO> updateResult(
            @PathVariable UUID id,
            @RequestParam Result result) {

        Match match = matchService.updateMatchResult(id, result);
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }

    // ========== MÉTODOS AUXILIARES ========== //

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuario no autenticado");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}