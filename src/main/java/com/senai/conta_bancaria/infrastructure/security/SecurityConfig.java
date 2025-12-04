package com.senai.conta_bancaria.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permitir acesso público a endpoints de autenticação e documentação
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ADMIN
                        .requestMatchers(HttpMethod.GET, "/gerentes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/gerentes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/gerentes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/gerentes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.POST, "/clientes/**").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAnyRole("ADMIN", "GERENTE")

                        .requestMatchers(HttpMethod.GET, "/contas/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/contas/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/contas/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/contas/**").hasRole("CLIENTE")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(usuarioDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}