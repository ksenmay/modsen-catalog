package by.may.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest (
        @NotBlank @Size(max = 255)
        String name,
        @Size(max = 255)
        String description) {}
