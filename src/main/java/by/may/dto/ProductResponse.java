package by.may.dto;

import by.may.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        LocalDate createdAt) {}
