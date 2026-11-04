package by.may.application.dto;

import by.may.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest (
        @Schema(description = "Логин, от 3 до 50 символов", example = "manager3")
        @NotBlank @Size(min = 3, max = 50)
        String username,
        @Schema(description = "Электронная почта", example = "manager3@local")
        @NotBlank @Email @Size(max = 255)
        String email,
        @Schema(description = "Пароль, от 8 до 100 символов", example = "test12345")
        @NotBlank @Size(min = 8, max = 100)
        String password,
        @Schema(description = "Роль пользователя", allowableValues = {"ADMIN", "MANAGER", "CUSTOMER"})
        @NotNull
        Role role) {}
