package com.gamehub.backend.service.impl;

import com.gamehub.backend.dto.security.LoginRequest;
import com.gamehub.backend.dto.security.TokenResponse;
import com.gamehub.backend.dto.security.UserRegistrationRequest;
import com.gamehub.backend.enums.Role;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import com.gamehub.backend.service.AuthService;
import com.gamehub.backend.service.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.util.ClassUtils.isPresent;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse createUser(@Valid UserRegistrationRequest userRegistrationRequest) {
        log.info("Intentando crear Usuario por Email: {}", userRegistrationRequest.getEmail());

        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            log.warn("Intento crear usuario con email existente: {}", userRegistrationRequest.getEmail());
            throw new IllegalArgumentException("El email ya esta registrado.");
        }
        try {
            User userToSave = mapToEntity(userRegistrationRequest);
            User userCreated = userRepository.save(userToSave);
            log.info("Usuario creado exitosamente con ID: {}", userCreated.getId());

            String roleName = userCreated.getRole().name();
            return jwtService.generateToken(userCreated.getId(), roleName);

        }catch (IllegalArgumentException e){
            throw e;
        }catch (Exception e){
            log.error("Error durante la creación del usuario para email {}: {}", userRegistrationRequest.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Error interno al crear usuario.", e);
        }
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        log.info("Intentando login para usuario: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->{
                    log.warn("Intento login fallido - Usuario no encontrado: {}", loginRequest.getEmail());
                    return new IllegalArgumentException("Usuario o contraseña inválidos.");
                });
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            log.warn("Intento de login fallido - Contraseña inválida para usuario: {}", loginRequest.getEmail());
            throw new IllegalArgumentException("Usuario o contraseña inválidos.");
        }
        String roleName = user.getRole().name();
        log.info("Login exitoso para usuario: {}", loginRequest.getEmail());
        return jwtService.generateToken(user.getId(), roleName);
    }

    private User mapToEntity(UserRegistrationRequest userRegistrationRequest){
        Role requestedRole = userRegistrationRequest.getRole();
        if(requestedRole == null){
            log.warn("El rol llegó nulo. Se asigna PLAYER por defecto para el usuario {}", userRegistrationRequest.getEmail());
            requestedRole = Role.PLAYER;
        }
        log.debug("Mapeando DTO a Entidad con Rol: {}", requestedRole);
        return User.builder()
                .email(userRegistrationRequest.getEmail())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .role(requestedRole)
                .username(userRegistrationRequest.getUsername())
                .build();
    }

}
