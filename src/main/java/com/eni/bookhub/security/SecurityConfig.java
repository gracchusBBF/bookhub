package com.eni.bookhub.security;

import com.eni.bookhub.auth.JwtAuthenticationFilter;
import com.eni.bookhub.auth.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/roles/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("api/permissions/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers("/api/loans/user/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers("/api/loans/active").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers("/api/loans/overdue").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers("/api/loans/stats/totalLoans").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers("/api/loans/stats/activeLoans").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/comments/{commentId}/status").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")// correspond à l'endpoint de màj du status
                        .requestMatchers("/api/comments/status/{status}").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_ADMIN")
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}