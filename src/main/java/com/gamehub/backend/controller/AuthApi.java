package com.gamehub.backend.controller;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para registro, login y verificación de usuarios")
public interface AuthApi {
    @Operation(summary = "Registro de usuario",
            description = "Crea una nueva cuenta de jugador (role=PLAYER)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado y token JWT generado"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "El email o username ya existen")
    })
    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest);

    @Operation(summary = "Inicio de sesión",
            description = "Autentica un usuario y genera token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token JWT generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "403", description = "Cuenta deshabilitada")
    })
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @Operation(summary = "Verificar autenticación",
            description = "Valida el token JWT y devuelve información básica del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
    })
    @GetMapping // Mapea a /api/auth (GET)
    @PreAuthorize("isAuthenticated()") // Requiere que el usuario esté autenticado
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId);


}
