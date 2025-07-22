package com.fitness.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            // Disable CSRF because Gateway is stateless
            .csrf(ServerHttpSecurity.CsrfSpec::disable)

            // Authorize requests
            .authorizeExchange(exchanges -> exchanges
//                .pathMatchers("/api/public/**").permitAll()  // Public endpoints
                .anyExchange().authenticated()               // All others need auth
            )

            // Enable JWT-based authentication
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
