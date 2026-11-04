package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReviewResponse(
        @Schema(description = "Текст отзыва", example = "Отличный товар, рекомендую")
        Long id,
        @Schema(description = "Оценка", example = "5")
        int rating,
        @Schema(description = "Текст отзыва", example = "Отличный товар, рекомендую")
        String comment,
        @Schema(description = "ID автора отзыва", example = "2")
        Long userId,
        @Schema(description = "ID продукта", example = "1")
        Long productId,
        @Schema(description = "Дата создания", example = "2026-11-03")
        LocalDate createdAt) {}
