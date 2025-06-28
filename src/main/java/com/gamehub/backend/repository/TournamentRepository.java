package com.gamehub.backend.repository;

import com.gamehub.backend.enums.Status;
import com.gamehub.backend.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    List<Tournament> findByStatus(Status status);

    // Verificar si un jugador está en un torneo
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Tournament t JOIN t.players p " +
            "WHERE t.id = :tournamentId AND p.id = :playerId")
    boolean existsPlayerInTournament(@Param("tournamentId") UUID tournamentId,
                                     @Param("playerId") UUID playerId);

    // Contar jugadores en un torneo
    @Query("SELECT SIZE(t.players) FROM Tournament t WHERE t.id = :tournamentId")
    int countPlayersInTournament(@Param("tournamentId") UUID tournamentId);
}
