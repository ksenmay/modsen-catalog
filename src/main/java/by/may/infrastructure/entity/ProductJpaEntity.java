package by.may.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE products SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "products")
@Getter
@Entity
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column
    private String description;

    @Setter
    @Column (nullable = false,  precision = 12, scale = 2)
    @Positive
    private BigDecimal price;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Setter
    private double averageRating = 0;

    public ProductJpaEntity(String name, String description, BigDecimal price, CategoryJpaEntity category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDate.now();
    }

}
