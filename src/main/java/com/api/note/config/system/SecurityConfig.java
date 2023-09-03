package com.api.note.config.system;

import com.api.note.config.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.api.note.config.system.DatabaseAuthenticationConfig.passwordEncoder;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig{

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
              .authorizeHttpRequests(auth ->
                      auth
                              .requestMatchers(antMatcher("/auth/**")).permitAll()
                              .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                              .requestMatchers(antMatcher("/users/**")).hasRole("ADMIN")
                              .anyRequest().authenticated()
              )
              .csrf(csrf -> csrf.disable())
              .headers(header -> header.frameOptions().disable())
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
              .build();

  }
  
}
