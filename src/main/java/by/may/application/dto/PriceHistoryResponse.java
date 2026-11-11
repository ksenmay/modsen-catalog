package by.may.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceHistoryResponse(
        @Schema(description = "ID записи истории", example = "1")
        Long id,
        @Schema(description = "ID продукта", example = "5")
        Long productId,
        @Schema(description = "Цена до изменения", example = "100.00")
        BigDecimal oldPrice,
        @Schema(description = "Цена после изменения", example = "120.00")
        BigDecimal newPrice,
        @Schema(description = "Дата и время изменения цены", example = "2026-11-04T12:00:00")
        LocalDateTime changedAt) {}