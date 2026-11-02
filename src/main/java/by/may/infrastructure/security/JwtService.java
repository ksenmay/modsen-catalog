package by.may.infrastructure.security;

import by.may.application.port.JwtPort;
import by.may.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService implements JwtPort {

    private final SecretKey key;
    private final long expirationTime;

    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    public String createJwt(CurrentUser currentUser) {

        Date now = new Date();

        return Jwts.builder()
                .subject(currentUser.username())
                .claim("id", currentUser.id())
                .claim("role", currentUser.role().name())
                .issuer("auth-service")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(this.key)
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
                Role.valueOf(claims.get("role", String.class))
        );
    }

    public Optional<CurrentUser> validate(String token) {
        try {
            return Optional.of(parseJwt(token));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
