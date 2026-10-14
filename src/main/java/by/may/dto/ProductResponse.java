package by.may.dto;

import by.may.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
//TODO пересмотреть
public record ProductResponse(String name, String description, BigDecimal price, Category category, LocalDate createdAt) {
}
