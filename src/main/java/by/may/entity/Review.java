package by.may.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    @Setter
    @Column(nullable = false)
    private String comment;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    @Setter
    private boolean isDeleted = false;

    public Review(int rating, String comment, User user, Product product, LocalDate createdAt) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }

    public void markDelete() {
        this.isDeleted = true;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
