package by.may.application.service.query;

import by.may.application.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryQuery {
    CategoryResponse getCategoryByName(String name);
    CategoryResponse getCategoryById(Long id);
    Page<CategoryResponse> getCategories(Pageable pageable);
}
