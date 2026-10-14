package by.may.dto.mapper;

import by.may.dto.CategoryCreateRequest;
import by.may.dto.CategoryResponse;
import by.may.entity.Category;

public class CategoryMapper {

    public Category toEntity(CategoryCreateRequest dto) {
        return new Category(
                dto.name(),
                dto.description()
        );
    }

    public CategoryResponse toDto(Category category) {
        return new CategoryResponse(
                category.getName(),
                category.getDescription()
        );
    }

}
