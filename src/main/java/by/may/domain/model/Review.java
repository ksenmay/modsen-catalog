package by.may.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class Review {
    private Long id;
    @Setter
    private int rating;
    @Setter
    private String comment;
    @Setter
    private Long userId;
    @Setter
    private Long productId;
    private LocalDate createdAt;

    public Review(Long id, int rating, String comment, Long userId, Long productId, LocalDate createdAt) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Review(int rating, String comment, Long userId, Long productId) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.productId = productId;
    }
}
