package by.may.dto;

import java.time.LocalDate;

public record ReviewResponse(
        Long id,
        int rating,
        String comment,
        Long userId,
        Long productId,
        LocalDate createdAt) {}
