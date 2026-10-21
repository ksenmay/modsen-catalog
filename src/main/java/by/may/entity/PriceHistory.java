package by.may.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "price_history")
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column (nullable = false,  precision = 12, scale = 2)
    @Positive
    private BigDecimal oldPrice;

    @Column (nullable = false,  precision = 12, scale = 2)
    @Positive
    private BigDecimal newPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    public PriceHistory(Product product, BigDecimal oldPrice, BigDecimal newPrice) {
        this.product = product;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    @PrePersist
    private void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

}
