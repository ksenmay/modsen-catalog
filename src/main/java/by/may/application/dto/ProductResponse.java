package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
        @Schema(description = "ID продукта", example = "1")
        Long id,
        @Schema(description = "Название продукта", example = "Телефон Apple iPhone 13")
        String name,
        @Schema(description = "Описание продукта", example = "Цвет: розовый, ОЗУ: 4Гб, память: 128Гб")
        String description,
        @Schema(description = "Цена", example = "1499.00")
        BigDecimal price,
        @Schema(description = "ID категории", example = "1")
        Long categoryId,
        @Schema(description = "Дата добавления продукта", example = "2026-11-03")
        LocalDate createdAt) {}
