package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @Schema(description = "Логин, от 3 до 50 символов", example = "ivan")
        @NotBlank
        @Size(min = 3, max = 50)
        String username,
        @Schema(description = "Электронная почта", example = "ivan@mail.ru")
        @NotBlank
        @Email
        @Size(max = 255)
        String email,
        @Schema(description = "Пароль, от 8 до 100 символов", example = "password123")
        @NotBlank
        @Size(min = 8, max = 100)
        String password){}
