package com.meditech.hospital.auth.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.meditech.hospital.auth.enums.TokenType;
import com.meditech.hospital.auth.enums.TokenExpiration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String SECRET;
    private final SecretKey key;

    private final Map<TokenType, Long> tokenExpirations = new HashMap<>();

    public JwtService(@Value("${jwt.secret}") String secret) {
        tokenExpirations.put(TokenType.ACCESS, 60 * 60 * 2L);
        tokenExpirations.put(TokenType.REFRESH, 60 * 60 * 24 * 7L);
        tokenExpirations.put(TokenType.PASSWORD_RESET, 60 * 10L);
        tokenExpirations.put(TokenType.VERIFICATION, 60 * 10L);

        this.SECRET = secret;
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email, TokenType tokenType) {
        return Jwts.builder()
                .subject(email)
                .claim("type", tokenType.name())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(TokenExpiration.get(tokenType))))
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public TokenType extractTokenType(String token) {
        String type = extractAllClaims(token).get("type", String.class);
        return TokenType.valueOf(type);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}
