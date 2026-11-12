package org.example.cpluspluslearningsite;

import org.example.cpluspluslearningsite.entity.User;
import org.example.cpluspluslearningsite.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class CPlusPlusLearningSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CPlusPlusLearningSiteApplication.class, args);
    }

    //создание тестовых данных во время запуска
    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@cpplearn.local")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .role("ROLE_ADMIN")
                        .createdAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                System.out.println(">>> Тестовый пользователь создан! Логин: admin | Пароль: admin123");
            }
        };
    }
}