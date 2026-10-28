package by.may.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest (
        @Min(1) @Max(5)
        int rating,
        @NotBlank @Size (max = 255)
        String comment) {}