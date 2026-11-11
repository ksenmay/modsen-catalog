package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshCreateRequest(
        @Schema(description = "Refresh-токен, полученный при входе", example = "3f1c9a2e-7b4d-4e8a-9c1f-2a6b5d8e0f11")
        @NotBlank
        String refreshToken) {}
