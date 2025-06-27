package com.gamehub.backend.controller;

import com.gamehub.backend.dto.PlayerDTO;
import com.gamehub.backend.dto.request.TournamentRequestDTO;
import com.gamehub.backend.dto.response.TournamentResponseDTO;
import com.gamehub.backend.model.Tournament;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament Management", description = "Endpoints para gestión de torneos")
public class TournamentController {

    private final TournamentService tournamentService;
    private final UserRepository userRepository;


    // ===== ENDPOINTS PRINCIPALES ===== //
    @Operation(summary = "Crear torneo", description = "Crea un nuevo torneo (requiere rol ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torneo creado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso no autorizado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestBody @Valid TournamentRequestDTO requestDTO) {
        Tournament tournament = new Tournament();
        tournament.setName(requestDTO.getName());
        tournament.setMaxPlayers(requestDTO.getMaxPlayers());

        Tournament created = tournamentService.createTournament(tournament);
        return ResponseEntity.ok(TournamentResponseDTO.fromEntity(created));
    }

    @Operation(summary = "Obtener torneo por ID", description = "Devuelve los detalles de un torneo específico")
    @ApiResponse(responseCode = "200", description = "Torneo encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                TournamentResponseDTO.fromEntity(tournamentService.getTournamentById(id))
        );
    }

    @Operation(summary = "Listar torneos", description = "Devuelve todos los torneos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de torneos")
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(
                tournamentService.getAllTournaments().stream()
                        .map(TournamentResponseDTO::fromEntity)
                        .toList()
        );
    }

    @Operation(summary = "Iniciar torneo", description = "Cambia el estado del torneo a IN_PROGRESS (requiere rol ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torneo iniciado"),
            @ApiResponse(responseCode = "400", description = "No hay suficientes jugadores")
    })
    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentResponseDTO> startTournament(@PathVariable UUID id) {
        return ResponseEntity.ok(
                TournamentResponseDTO.fromEntity(tournamentService.startTournament(id))
        );
    }

    @Operation(summary = "Unirse a torneo", description = "Registra un jugador en el torneo (requiere rol PLAYER)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador registrado"),
            @ApiResponse(responseCode = "400", description = "Torneo ya iniciado/finalizado")
    })
    @PostMapping("/{id}/join")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<TournamentResponseDTO> joinTournament(@PathVariable UUID id) {
        User currentUser = getAuthenticatedUser();
        return ResponseEntity.ok(
                TournamentResponseDTO.fromEntity(tournamentService.joinTournament(id, currentUser))
        );
    }

    @Operation(summary = "Obtener jugadores", description = "Lista de jugadores inscritos en el torneo")
    @ApiResponse(responseCode = "200", description = "Lista de jugadores")
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerDTO>> getTournamentPlayers(@PathVariable UUID id) {
        return ResponseEntity.ok(
                tournamentService.getTournamentPlayers(id).stream()
                        .map(PlayerDTO::fromEntity)
                        .toList()
        );
    }

    @Operation(summary = "Finalizar torneo", description = "Cambia el estado del torneo a FINISHED (requiere rol ADMIN)")
    @ApiResponse(responseCode = "200", description = "Torneo finalizado")
    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentResponseDTO> finishTournament(@PathVariable UUID id) {
        return ResponseEntity.ok(
                TournamentResponseDTO.fromEntity(tournamentService.finishTournament(id))
        );
    }

    // ===== MÉTODOS AUXILIARES ===== //

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuario no autenticado");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}