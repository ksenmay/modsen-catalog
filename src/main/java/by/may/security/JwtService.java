package by.may.security;

import by.may.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(CurrentUser currentUser) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(currentUser.username())
                .claim("id", currentUser.id())
                .claim("role", currentUser.role())
                .issuer("auth-service")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();
    }

    public CurrentUser parseJwt(String jwtString) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtString)
                .getPayload();

        return new CurrentUser(
                claims.get("id", Long.class),
                claims.getSubject(),
                claims.get("role", Role.class)
        );
    }
}
