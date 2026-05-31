package com.main.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =  "mysecretkeymysecretkeymysecretkey123";

    private static final long EXPIRATION = 1000 * 60 * 60;

    private SecretKey getSignKey() {

        return Keys.hmacShaKeyFor(
                SECRET.getBytes());
    }

    public String generateToken( String username) {

        return Jwts.builder().subject(username).issuedAt(new Date())
                .expiration( new Date(System.currentTimeMillis()+ EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {

        Claims claims =
                Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(
            String token) {

        try {

            Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}