package by.may.mapper;

import by.may.dto.CategoryCreateRequest;
import by.may.dto.CategoryResponse;
import by.may.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateRequest dto) {
        return new Category(
                dto.name(),
                dto.description()
        );
    }

    public CategoryResponse toDto(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

}
