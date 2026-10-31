package by.may.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank @Size(max = 255)
        String name,
        @Size(max = 255)
        String description,
        @NotNull @Positive @Digits(integer = 10, fraction = 2)
        BigDecimal price,
        @NotNull
        Long categoryId) {}
