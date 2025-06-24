package com.gamehub.backend.security;

import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        UUID userId = null;
        String roleName = null;

        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.trace("No JWT token found in Authorization header or header does not start with Bearer.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            if (jwtService.isExpired(jwt)) {
                log.warn("JWT token has expired.");
                filterChain.doFilter(request, response);
                return;
        }
            userId = jwtService.extractUserId(jwt);
            roleName = jwtService.extractRole(jwt);
            log.debug("Token validated. UserID: {}, Role: {}", userId, roleName);

    } catch (Exception e){
            log.error("Error processing JWT token: {}", e.getMessage(), e); // Loggear la excepción completa
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        if(userId != null && StringUtils.hasText(roleName) && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty() || !userOptional.get().isEnabled()){
                log.warn("User ID {} from token not found in DB or is not active/enabled.", userId);
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            User user = userOptional.get();
            String principal = user.getEmail();

            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
            log.debug("Creating Authentication object with principal: {} and authorities: {}", principal, authorities);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );

            // Establecer la autenticación en el contexto de seguridad
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

            log.info("User {} successfully authenticated with role {}.", principal, roleName);

            // (Opcional) Agregar ID de usuario a los atributos de la solicitud
            request.setAttribute("X-User-Id", userId);

        }else {
            // Log si ya hay autenticación o si faltan datos del token
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.trace("Security context already contains authentication. Skipping token authentication.");
            } else {
                log.warn("Could not authenticate user. UserID or Role missing from token.");
            }
        }

        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);

    }

}
