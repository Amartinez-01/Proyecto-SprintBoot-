package com.gamehub.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title("GameHub Tournaments API")
                .version("1.0")
                .description(String.join("\n",
                        "API de gestión de torneos de videojuegos online.",
                        "Funcionalidades principales:",
                        "- Creación y gestión de torneos (ADMIN)",
                        "- Inscripción de jugadores (PLAYER)",
                        "- Generación automática de partidas",
                        "- Sistema de resultados y rankings",
                        "- Chat básico durante los torneos",
                        "",
                        "Roles del sistema:",
                        "- ADMIN: Creación de torneos y gestión avanzada",
                        "- PLAYER: Participación en torneos"
                ));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                    Autenticación JWT. Obtenga el token:
                    1. Registro: POST /api/auth/register
                    2. Login: POST /api/auth/login
                    Incluya el token en las cabeceras como: 'Authorization: Bearer {token}'""");
    }
}