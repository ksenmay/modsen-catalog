package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(
        @Schema(description = "Название продукта", example = "Телефон Apple iPhone 13")
        @NotBlank @Size(max = 255)
        String name,
        @Schema(description = "Описание продукта", example = "Цвет: розовый, ОЗУ: 4Гб, память: 128Гб")
        @Size(max = 255)
        String description,
        @Schema(description = "Цена больше нуля, до двух знаков после запятой", example = "1499.00")
        @NotNull @Positive @Digits(integer = 10, fraction = 2)
        BigDecimal price,
        @Schema(description = "ID категории, к которой относится продукт", example = "1")
        @NotNull
        Long categoryId) {}
