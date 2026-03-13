package com.thiago.imageprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thiago.imageprocessor.security.JwtAuthenticationFilter;
import com.thiago.imageprocessor.service.JwtService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // ELIMINADO: @Bean public JwtAuthenticationFilter jwtAuthenticationFilter() { ... }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Creamos la instancia local para la cadena de seguridad
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
            .csrf(csrf -> csrf.disable()) // Vital para permitir POST desde curl/Postman
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usamos cookies
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll() // Permite login y register
                .requestMatchers("/api/images/upload").authenticated() // Protege la subida
                .anyRequest().authenticated()
            )
            // Agregamos el filtro manualmente aquí
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}