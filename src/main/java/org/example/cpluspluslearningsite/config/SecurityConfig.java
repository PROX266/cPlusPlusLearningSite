package org.example.cpluspluslearningsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        //Разрешает всем доступ к главной странице, регистрации и статике
                        .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        //Все остальные страницы требуют авторизации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        //временная дефолтная форма входа
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}