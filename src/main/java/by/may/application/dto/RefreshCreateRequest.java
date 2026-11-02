package by.may.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshCreateRequest(
        @NotBlank String refreshToken) {}
