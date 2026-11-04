package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(description = "ID категории", example = "1")
        Long id,
        @Schema(description = "Название категории", example = "Техника")
        String name,
        @Schema(description = "Описание категории", example = "Электроника и бытовые приборы")
        String description) {}
