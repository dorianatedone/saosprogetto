package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/login", "/css/**").permitAll()
              .anyRequest().authenticated()
          )
          .oauth2Login(oauth2 -> oauth2
              .loginPage("/login")
              .defaultSuccessUrl("/user", true)
              .failureUrl("/login?error=true")
          )
          .logout(logout -> logout
              .logoutSuccessUrl("/login?logout=true")
          );
        return http.build();
    }
}
