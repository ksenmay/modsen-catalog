package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginCreateRequest(
        @Schema(description = "Логин пользователя", example = "admin")
        @NotBlank String username,
        @Schema(description = "Пароль пользователя", example = "admin12345")
        @NotBlank String password) {}
