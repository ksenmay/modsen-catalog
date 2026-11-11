package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenPairResponse(
        @Schema(description = "Access-токен для заголовка Authorization: Bearer", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,
        @Schema(description = "Refresh-токен для обновления пары", example = "3f1c9a2e-7b4d-4e8a-9c1f-2a6b5d8e0f11")
        String refreshToken) {}
