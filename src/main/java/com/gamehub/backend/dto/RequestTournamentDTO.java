package com.gamehub.backend.dto;

import com.gamehub.backend.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTournamentDTO {

    @NotBlank(message = "El nombre del torneo es obligatorio")
    private String name;

    @Min(value = 2, message = "Debe haber al menos 2 jugadores")
    private int maxPlayers;

    private Status status;
}

