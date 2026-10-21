package by.may.event;

import java.math.BigDecimal;

public record PriceChangeEvent(
        String productName,
        BigDecimal oldPrice,
        BigDecimal newPrice) {}
