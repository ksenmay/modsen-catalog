package by.may.domain.event;

import java.math.BigDecimal;

public record PriceChangedEvent(
        String productName,
        BigDecimal oldPrice,
        BigDecimal newPrice) {}
