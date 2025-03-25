package com.dalhousie.Neighbourly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    @Profile("!local")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            configureCsrf(http);
            configureAuthorization(http);
            configureSessionManagement(http);
            configureJwtFilter(http);
            configureAuthenticationProvider(http);

            return http.build();
        }

        private void configureCsrf(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
        }

        private void configureAuthorization(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(req -> req
                    .requestMatchers("/api/**").permitAll()
                    .anyRequest().authenticated()
            );
        }

        private void configureSessionManagement(HttpSecurity http) throws Exception {
            http.sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        }

        private void configureJwtFilter(HttpSecurity http) throws Exception {
            http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        }

        private void configureAuthenticationProvider(HttpSecurity http) throws Exception {
            http.authenticationProvider(authenticationProvider);
        }
    }





