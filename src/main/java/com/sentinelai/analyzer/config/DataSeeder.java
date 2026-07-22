package com.sentinelai.analyzer.config;

import com.sentinelai.analyzer.entity.User;
import com.sentinelai.analyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .build();
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("viewer").isEmpty()) {
            User viewer = User.builder()
                    .username("viewer")
                    .password(passwordEncoder.encode("viewer123"))
                    .role("VIEWER")
                    .build();
            userRepository.save(viewer);
        }
    }
}