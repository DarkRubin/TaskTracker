package org.roadmap.tasktrackerbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Setter
@Service
public class JwtService {

    @Value("${jwt.key}")
    private String key;

    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    public String generateToken(String email, String password) {
        Map<String, Object> claims = Map.of(
                "email", email,
                "password", password);
        return Jwts.builder().claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(), Jwts.SIG.HS256).compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
