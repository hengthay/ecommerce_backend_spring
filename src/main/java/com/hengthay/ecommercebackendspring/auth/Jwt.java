package com.hengthay.ecommercebackendspring.auth;

import com.hengthay.ecommercebackendspring.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    // return expiration token
    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    // get user id from object
    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    // get user role from object
    public Role getUserRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    @Override
    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact(); // use compact to convert it to string
    }
}
