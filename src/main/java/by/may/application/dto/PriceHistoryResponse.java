package by.may.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceHistoryResponse(
        Long id,
        Long productId,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        LocalDateTime changedAt) {

}