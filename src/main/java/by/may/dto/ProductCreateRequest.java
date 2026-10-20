package by.may.dto;

import java.math.BigDecimal;

public record ProductCreateRequest(
        String name,
        String description,
        BigDecimal price,
        Long categoryId) {}
