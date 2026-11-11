package by.may.application.dto;

import by.may.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserResponse(
        @Schema(description = "ID пользователя", example = "1")
        Long id,
        @Schema(description = "Логин", example = "admin")
        String username,
        @Schema(description = "Электронная почта", example = "admin@local")
        String email,
        @Schema(description = "Роль", allowableValues = {"ADMIN", "MANAGER", "CUSTOMER"})
        Role role,
        @Schema(description = "Дата регистрации", example = "2026-11-04")
        LocalDate createdAt) {}
