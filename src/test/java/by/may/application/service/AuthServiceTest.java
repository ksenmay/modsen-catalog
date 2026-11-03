package by.may.application.service;

import by.may.application.dto.TokenPairResponse;
import by.may.application.port.JwtPort;
import by.may.application.port.PasswordHasher;
import by.may.application.port.UserRepositoryPort;
import by.may.domain.Role;
import by.may.domain.exception.ForbiddenException;
import by.may.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordHasher passwordHasher;
    @Mock private JwtPort jwt;
    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsTokenPairForValidCredentials() {
        User user = new User(1L, "ivan", "ivan@mail.ru", "hash", Role.CUSTOMER, LocalDate.now());
        when(userRepositoryPort.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(passwordHasher.matches("pass", "hash")).thenReturn(true);
        when(jwt.createJwt(any())).thenReturn("access-token");

        TokenPairResponse result = authService.login("ivan", "pass");

        assertEquals("access-token", result.accessToken());
        assertNotNull(result.refreshToken());
        verify(userRepositoryPort).save(user);
    }

    @Test
    void loginThrowsForWrongPassword() {
        User user = new User(1L, "ivan", "ivan@mail.ru", "hash", Role.CUSTOMER, LocalDate.now());
        when(userRepositoryPort.findByUsername("ivan")).thenReturn(Optional.of(user));
        when(passwordHasher.matches("wrong", "hash")).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> authService.login("ivan", "wrong"));
        verify(jwt, never()).createJwt(any());
    }

    @Test
    void refreshReturnsNewPairAndRotatesToken() {
        User user = new User(1L, "ivan", "ivan@mail.ru", "hash", Role.CUSTOMER, LocalDate.now());
        user.setRefreshToken("old");
        user.setRefreshTokenExpiryTime(LocalDateTime.now().plusDays(1));
        when(userRepositoryPort.findByRefreshToken("old")).thenReturn(Optional.of(user));
        when(jwt.createJwt(any())).thenReturn("new-access");

        TokenPairResponse result = authService.refresh("old");

        assertNotEquals("old", result.refreshToken());
        assertNotEquals("old", user.getRefreshToken());
    }

    @Test
    void refreshThrowsForExpiredToken() {
        User user = new User(1L, "ivan", "ivan@mail.ru", "hash", Role.CUSTOMER, LocalDate.now());
        user.setRefreshToken("old");
        user.setRefreshTokenExpiryTime(LocalDateTime.now().minusMinutes(1));
        when(userRepositoryPort.findByRefreshToken("old")).thenReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> authService.refresh("old"));
    }

    @Test
    void refreshThrowsForUnknownToken() {
        when(userRepositoryPort.findByRefreshToken("bad")).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> authService.refresh("bad"));
    }
}