package by.may.application.service;

import by.may.application.dto.TokenPairResponse;
import by.may.application.port.EventPublisher;
import by.may.application.port.JwtPort;
import by.may.application.port.PasswordHasher;
import by.may.application.port.UserRepositoryPort;
import by.may.domain.event.UserLoggedInEvent;
import by.may.domain.exception.ForbiddenException;
import by.may.domain.exception.UserNotFoundException;
import by.may.domain.model.User;
import by.may.infrastructure.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final long REFRESH_TOKEN = 900;

    private final UserRepositoryPort userRepositoryPort ;
    private final PasswordHasher passwordHasher;
    private final JwtPort jwt;
    private final EventPublisher publisher;

    @Transactional
    public TokenPairResponse login(String username, String password) {
        User user = userRepositoryPort.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );

        if (!passwordHasher.matches(password, user.getPasswordHash())) {
            log.error("неверный логин {} или пароль", username);
            throw new ForbiddenException("Invalid login or password");
        }

        log.info("пользователь {} вошёл", username);
        publisher.publish(new UserLoggedInEvent(username));
        return issueToken(user);
    }

    @Transactional
    public TokenPairResponse refresh(String refreshToken) {
        User user = userRepositoryPort.findByRefreshToken(refreshToken).orElseThrow(
                () -> new ForbiddenException("Invalid refresh token")
        );
        if (user.getRefreshTokenExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Refresh token has expired");
        }
        return issueToken(user);
    }

    private TokenPairResponse issueToken(User user) {
        String access = jwt.createJwt(
                new CurrentUser(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                )
        );
        String refresh = UUID.randomUUID().toString();
        user.setRefreshToken(refresh);
        user.setRefreshTokenExpiryTime(LocalDateTime.now().plusSeconds(REFRESH_TOKEN));
        userRepositoryPort.save(user);

        return new TokenPairResponse(access, refresh);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepositoryPort.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        user.setRefreshToken(null);
        user.setRefreshTokenExpiryTime(null);
        userRepositoryPort.save(user);
        log.info("пользователь {} вышел", user.getUsername());
    }
}
