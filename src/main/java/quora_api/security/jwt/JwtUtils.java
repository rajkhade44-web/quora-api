package quora_api.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

@Value("${app.jwt.access-expiration-ms}")
    private int accessExpirationMs;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSecretKey())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(getSecretKey())
                .build().parseSignedClaims(token)
                .getPayload();
        
        return UUID.fromString(claim.getSubject());
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }



}
