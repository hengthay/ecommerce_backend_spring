package com.hengthay.ecommercebackendspring.auth;

import com.hengthay.ecommercebackendspring.users.Role;
import com.hengthay.ecommercebackendspring.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    public Jwt getAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt getRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    // To generate token
    private Jwt generateToken(User user, long tokenExpiration) {
        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("username", user.getUsername())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }


    // To check if token valid
    public Jwt validateToken(String token) {
        try {
            // If getClaims succeeds, JJWT has already verified the signature
            // and confirmed the expiration date is strictly in the future.
            var claims = getClaims(token);

            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    // To check and parse token
    private Claims getClaims(String token) {
        // verify with secret key and check token if matching
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getUserRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}
