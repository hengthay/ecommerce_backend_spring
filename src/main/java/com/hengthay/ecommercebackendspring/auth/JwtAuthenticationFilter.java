package com.hengthay.ecommercebackendspring.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract request header from client
        var authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Do other filter
            filterChain.doFilter(request, response);
            return;
        }

        var tokenString = authHeader.substring(7);
        var jwt = jwtService.validateToken(tokenString);

        // if token expired do other filter
        if(jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Create the Spring Security authentication token.
        // Add SimpleGrantedAuthority to tell spring
        // which role able to access protected resource
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getUserRole()))
        );

        // Attach web-specific metadata (client IP, session ID) to the token.
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // Store the authenticated user in the current thread's SecurityContext.
        // we can use it later
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // proceed next filter
        filterChain.doFilter(request, response);
    }
}
