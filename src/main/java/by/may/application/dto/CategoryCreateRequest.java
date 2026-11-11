package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest (
        @Schema(description = "Название категории, уникальное", example = "Техника")
        @NotBlank @Size(max = 255)
        String name,
        @Schema(description = "Описание категории", example = "Электроника и бытовые приборы")
        @Size(max = 255)
        String description) {}
