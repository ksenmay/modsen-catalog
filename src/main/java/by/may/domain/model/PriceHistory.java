package by.may.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PriceHistory {
    private Long id;
    @Setter
    private Long productId;
    @Setter
    private BigDecimal oldPrice;
    @Setter
    private BigDecimal newPrice;
    private LocalDateTime changedAt;

    public PriceHistory(Long productId, BigDecimal oldPrice, BigDecimal newPrice) {
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    public PriceHistory(Long id, Long productId, BigDecimal oldPrice, BigDecimal newPrice, LocalDateTime changedAt) {
        this.id = id;
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changedAt = changedAt;
    }
}
