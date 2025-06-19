package com.gamehub.backend.configSegutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // indica a sprint sprint que esta clase definiciones de beans
@EnableWebSecurity // organiza el entorno de seguridad para gestionar las solicitudes http
@EnableMethodSecurity(prePostEnabled = true) //me da autorización para usar anotaciones como @PreAuthorize y PostAuthorize
                                            //aplica reglas de seguridad más específicas a nivel de métodos, facilitando
                                           // un control granular de permisos.

public class SegurityConfig {

     //bean usado para cifrar todas las contraseñas
    // para no almacenar contraseñas en texto plano
    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

/* objetivo del metodo que configura la seguridad de una aplicación Spring Boot utilizando JWT para la autenticación y autorización. Desactiva
la protección CSRF, establece una gestión de sesiones sin estado, define rutas públicas y protegidas, y añade filtros
 para manejar la autenticación y autorización de manera efectiva.
 *no lo logre terminar estoy confundido*
 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil, CustomUserDetailsService uds) throws Exception {

    }


}

