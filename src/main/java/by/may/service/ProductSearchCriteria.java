package by.may.service;

import java.math.BigDecimal;

public record ProductSearchCriteria(
        String name,
        Long categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Double minRating,
        ProductSortField sortBy,
        boolean asc
) {}
