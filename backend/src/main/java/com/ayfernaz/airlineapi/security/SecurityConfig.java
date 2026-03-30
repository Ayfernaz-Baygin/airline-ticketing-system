package com.ayfernaz.airlineapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF kapalı (REST API için doğru)
                .csrf(csrf -> csrf.disable())

                // Session yok → JWT stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Endpoint güvenlik kuralları
                .authorizeHttpRequests(auth -> auth
                        // Public endpointler
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/flights/search").permitAll()
                        .requestMatchers("/api/v1/flights/check-in").permitAll()

                        // Protected endpointler
                        .requestMatchers("/api/v1/flights").authenticated()
                        .requestMatchers("/api/v1/flights/upload").authenticated()
                        .requestMatchers("/api/v1/flights/tickets/buy").authenticated()
                        .requestMatchers("/api/v1/flights/*/passengers").authenticated()

                        // Diğer her şey serbest
                        .anyRequest().permitAll()
                )

                // JWT filter ekleniyor
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}