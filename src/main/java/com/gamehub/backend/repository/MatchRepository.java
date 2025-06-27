package com.gamehub.backend.repository;

import com.gamehub.backend.enums.Result;
import com.gamehub.backend.model.Match;
import com.gamehub.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    // Consulta corregida para listar partidas por torneo ordenadas por ronda
    List<Match> findByTournamentIdOrderByRoundAsc(UUID tournamentId);

    // Consulta corregida para partidas por torneo y ronda
    List<Match> findByTournamentIdAndRound(UUID tournamentId, int round);

    // Consulta corregida para partidas pendientes
    List<Match> findByTournamentIdAndResult(UUID tournamentId, Result result);

    // Contar partidas pendientes (correcto)
    long countByTournamentIdAndResult(UUID tournamentId, Result result);

    // Obtener última ronda - consulta corregida
    @Query("SELECT MAX(m.round) FROM Match m WHERE m.tournament.id = :tournamentId")
    Optional<Integer> findMaxRoundByTournamentId(@Param("tournamentId") UUID tournamentId);

    // Consulta de ganadores corregida
    @Query("SELECT CASE " +
            "WHEN m.result = 'PLAYER1_WIN' THEN m.player1 " +
            "WHEN m.result = 'PLAYER2_WIN' THEN m.player2 " +
            "ELSE null END " +
            "FROM Match m " +
            "WHERE m.tournament.id = :tournamentId AND m.round = :round " +
            "AND m.result IN (com.gamehub.backend.enums.Result.PLAYER1_WIN, " +
            "com.gamehub.backend.enums.Result.PLAYER2_WIN)")
    List<User> findWinnersByRound(@Param("tournamentId") UUID tournamentId,
                                  @Param("round") int round);
}