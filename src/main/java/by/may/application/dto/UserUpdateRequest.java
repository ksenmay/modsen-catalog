package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
        @Schema(description = "Новый логин, от 3 до 50 символов", example = "ivan_new")
        @NotBlank @Size(min = 3, max = 50)
        String username,
        @Schema(description = "Новая электронная почта", example = "ivan@mail.ru")
        @NotBlank @Email @Size(max = 255)
        String email){}
