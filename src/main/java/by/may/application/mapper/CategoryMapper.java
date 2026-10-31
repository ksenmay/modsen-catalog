package by.may.application.mapper;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.dto.CategoryResponse;
import by.may.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toModel(CategoryCreateRequest dto) {
        return new Category(
                dto.name(),
                dto.description()
        );
    }

    public CategoryResponse toDto(Category model) {
        return new CategoryResponse(
                model.getId(),
                model.getName(),
                model.getDescription()
        );
    }

}
