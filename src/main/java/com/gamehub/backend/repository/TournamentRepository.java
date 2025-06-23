package com.gamehub.backend.repository;

import com.gamehub.backend.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {

    boolean existsByName(String name);
}
