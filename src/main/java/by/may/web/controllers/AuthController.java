package by.may.web.controllers;

import by.may.application.dto.LoginCreateRequest;
import by.may.application.dto.RefreshCreateRequest;
import by.may.application.dto.TokenPairResponse;
import by.may.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenPairResponse login(@Valid @RequestBody LoginCreateRequest dto) {
        return authService.login(dto.username(), dto.password());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenPairResponse refresh(@Valid @RequestBody RefreshCreateRequest dto) {
        return authService.refresh(dto.refreshToken());
    }
}
