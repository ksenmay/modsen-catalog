package by.may.dto;

import by.may.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewCreateRequest (
        @Min(1) @Max(5)
        int rating,
        String comment,
        User user,
        Long productId) {}