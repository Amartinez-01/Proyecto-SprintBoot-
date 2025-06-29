package com.gamehub.backend.config;

import com.gamehub.backend.enums.Role;
import com.gamehub.backend.model.User;
import com.gamehub.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadDefaultUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear usuario ADMIN si no existe
            if (!userRepository.existsByEmail("admin@mail.com")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@mail.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.ADMIN)
                        .rank("MASTER")
                        .wins(10)
                        .losses(2)
                        .points(1500)
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado: admin@mail.com / 1234");
            }

            // Crear usuario PLAYER si no existe
            if (!userRepository.existsByEmail("player1@mail.com")) {
                User player = User.builder()
                        .username("player1")
                        .email("player1@mail.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.PLAYER)
                        .rank("NOVICE")
                        .wins(3)
                        .losses(5)
                        .points(750)
                        .build();
                userRepository.save(player);
                System.out.println("✅ Usuario PLAYER creado: player1@mail.com / 1234");
            }
        };
    }
}
