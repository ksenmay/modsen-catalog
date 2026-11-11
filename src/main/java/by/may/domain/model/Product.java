package by.may.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Product {
    private Long id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private BigDecimal price;
    @Setter
    private Long categoryId;
    private LocalDate createdAt;
    @Setter
    private double averageRating = 0;

    public Product(Long id, String name, String description, BigDecimal price, Long categoryId, LocalDate createdAt, double averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.averageRating = averageRating;
    }

    public Product(String name, String description, BigDecimal price, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }
}
