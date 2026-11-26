package com.hengthay.ecommercebackendspring.config;

import com.hengthay.ecommercebackendspring.auth.JwtAuthenticationFilter;
import com.hengthay.ecommercebackendspring.users.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) {
        return config.getAuthenticationManager();
    }
    // Implement security filtering
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> {
                    c.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    // auth endpoints
                    c.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh").permitAll();
                    c.requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated();

                    // Products endpoints
                    c.requestMatchers(HttpMethod.GET, "/api/products/all","/api/products/active", "/api/products/{id}").permitAll();
                    c.requestMatchers(HttpMethod.GET, "/api/products").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.POST, "/api/products", "/api/products/{id}/restore").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole(Role.ADMIN.name());

                    // Categories endpoints
                    c.requestMatchers(HttpMethod.GET, "/api/categories/all-status", "/api/categories/in-active").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/{id}").permitAll();
                    c.requestMatchers(HttpMethod.POST,"/api/categories", "/api/categories/{id}/restore").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.PUT,"/api/categories/{id}").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.DELETE,"/api/categories/{id}").hasRole(Role.ADMIN.name());

                    // User endpoints
                    c.requestMatchers(HttpMethod.POST, "/api/users").permitAll();
                    c.requestMatchers(HttpMethod.GET,"/api/users","/api/users/active", "/api/users/{id}").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.POST,"/api/users/{id}/activate-user").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.PATCH,"/api/users/{id}/role").hasRole(Role.ADMIN.name());

                    // Authenticated user-owned
                    c.requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated();
                    c.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").authenticated();
                    c.requestMatchers(HttpMethod.POST, "/api/users/{id}/change-password").authenticated();

                    // Carts and Orders endpoints
                    c.requestMatchers(HttpMethod.GET, "/api/carts").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.DELETE, "/api/carts/{cartId}").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.GET, "/api/orders").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.PATCH, "/api/orders/{orderId}").hasRole(Role.ADMIN.name());

                    // Addresses endpoints
                    c.requestMatchers(HttpMethod.GET, "/api/addresses").hasRole(Role.ADMIN.name());
                    c.requestMatchers(HttpMethod.PATCH, "/api/addresses/{id}/activate").hasRole(Role.ADMIN.name());

                    c.anyRequest().authenticated();
                })
                // Check token before access to endpoints
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // This exception use to handle user authenticated but does not have permission to access
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json");
                        ErrorDto error = new ErrorDto(401, "Unauthorized", null, Instant.now());
                        objectMapper.writeValue(response.getOutputStream(), error);
                    }));
                    c.accessDeniedHandler(((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        ErrorDto error = new ErrorDto(403, "Access denied", null, Instant.now());
                        objectMapper.writeValue(response.getOutputStream(), error);
                    }));
                });

        return http.build();
    }
}
