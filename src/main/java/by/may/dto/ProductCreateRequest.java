package by.may.dto;

import by.may.entity.Category;

import java.math.BigDecimal;

//TODO пересмотреть
public record ProductCreateRequest(String name, String description, BigDecimal price, Category category) {
}
