package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest (
        @Schema(description = "Оценка от 1 до 5", example = "5")
        @Min(1) @Max(5)
        int rating,
        @Schema(description = "Текст отзыва", example = "Отличный товар, рекомендую")
        @NotBlank @Size (max = 255)
        String comment,
        Long userId,
        Long productId) {}