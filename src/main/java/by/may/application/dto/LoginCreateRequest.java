package by.may.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginCreateRequest(
        @NotBlank String username,
        @NotBlank String password) {}
