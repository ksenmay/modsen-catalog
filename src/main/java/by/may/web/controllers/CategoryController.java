package by.may.web.controllers;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.dto.CategoryResponse;
import by.may.application.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryResponse> getAll(@PageableDefault(size=10, sort="name") Pageable pageable) {
        return categoryService.getCategories(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest dto) {
        return categoryService.createCategory(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest dto) {
        return categoryService.updateCategory(id, dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
