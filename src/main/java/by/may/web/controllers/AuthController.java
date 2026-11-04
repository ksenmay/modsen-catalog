package by.may.web.controllers;

import by.may.application.dto.LoginCreateRequest;
import by.may.application.dto.RefreshCreateRequest;
import by.may.application.dto.TokenPairResponse;
import by.may.application.service.AuthService;
import by.may.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Аутентификация", description = "Вход и обновление пары токенов")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Вход", description = "Возвращает пару access и refresh токенов")
    @PostMapping("/login")
    @SecurityRequirements
    @ResponseStatus(HttpStatus.OK)
    public TokenPairResponse login(@Valid @RequestBody LoginCreateRequest dto) {
        return authService.login(dto.username(), dto.password());
    }

    @Operation(summary = "Обновление токенов",
            description = "По refresh-токену выдаёт новую пару, старый refresh перестаёт работать")
    @PostMapping("/refresh")
    @SecurityRequirements
    @ResponseStatus(HttpStatus.OK)
    public TokenPairResponse refresh(@Valid @RequestBody RefreshCreateRequest dto) {
        return authService.refresh(dto.refreshToken());
    }

    @Operation(summary = "Выход",
            description = "Сбрасывает refresh-токен текущего пользователя")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal CurrentUser currentUser) {
        authService.logout(currentUser.id());
    }
}
