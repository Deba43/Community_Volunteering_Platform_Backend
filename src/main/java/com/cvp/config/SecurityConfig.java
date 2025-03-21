package com.cvp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/organization/**", "/organizations/**").permitAll()
                        .requestMatchers("/task/**").permitAll()
                        .requestMatchers("/users/register", "/users/login", "/users/forgot-password",
                                "/users/reset-password")
                        .permitAll()

                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .requestMatchers("/users/**").permitAll()

                        .anyRequest().authenticated()
                        .requestMatchers("/tasksignup/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
