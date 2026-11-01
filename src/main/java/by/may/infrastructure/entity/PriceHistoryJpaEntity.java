package by.may.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "price_history")
public class PriceHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity productJpaEntity;

    @Column (nullable = false,  precision = 12, scale = 2)
    @Positive
    private BigDecimal oldPrice;

    @Column (nullable = false,  precision = 12, scale = 2)
    @Positive
    private BigDecimal newPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name = "is_deleted", nullable = false)
    @Setter
    private boolean deleted = false;

    public PriceHistoryJpaEntity(ProductJpaEntity productJpaEntity, BigDecimal oldPrice, BigDecimal newPrice) {
        this.productJpaEntity = productJpaEntity;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    public void markDelete() {
        this.deleted = true;
    }

    @PrePersist
    private void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}
