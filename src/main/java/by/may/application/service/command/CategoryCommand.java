package by.may.application.service.command;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.dto.CategoryResponse;

public interface CategoryCommand {
    CategoryResponse createCategory(CategoryCreateRequest dto);
    CategoryResponse updateCategory(Long id, CategoryCreateRequest dto);
    void deleteCategory(Long id);
}
