package by.may.application.port;

import by.may.infrastructure.security.CurrentUser;

import java.util.Optional;

public interface JwtPort {
    String createJwt(CurrentUser currentUser);
    CurrentUser parseJwt(String jwtString);
    Optional<CurrentUser> validate(String token);
}
