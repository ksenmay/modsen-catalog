package by.may.infrastructure.repository;

import by.may.infrastructure.entity.ProductJpaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecifications {

    private ProductSpecifications() {}

    public static Specification<ProductJpaEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<ProductJpaEntity> inCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<ProductJpaEntity> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("price"), min, max);
            }
            if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min);
            }
            if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), max);
            }
            return null;
        };
    }

    public static Specification<ProductJpaEntity> minRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), rating);
        };
    }

}
