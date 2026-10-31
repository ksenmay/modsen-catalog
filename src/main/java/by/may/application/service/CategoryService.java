package by.may.application.service;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.dto.CategoryResponse;
import by.may.application.port.CategoryRepositoryPort;
import by.may.application.port.ProductRepositoryPort;
import by.may.application.service.command.CategoryCommand;
import by.may.application.service.query.CategoryQuery;
import by.may.domain.exception.CategoryNotEmpty;
import by.may.application.mapper.CategoryMapper;
import by.may.domain.model.Category;
import by.may.domain.exception.CategoryAlreadyExistsException;
import by.may.domain.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryCommand, CategoryQuery {

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final CategoryMapper categoryMapper;
    private final ProductRepositoryPort productRepositoryPort;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest dto) {
        if (categoryRepositoryPort.existsByName(dto.name())) {
            throw new CategoryAlreadyExistsException(dto.name());
        }
        Category category = categoryMapper.toModel(dto);
        return categoryMapper.toDto(categoryRepositoryPort.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryCreateRequest dto) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        if (!category.getName().equals(dto.name()) && categoryRepositoryPort.existsByName(dto.name())) {
            throw new CategoryAlreadyExistsException(dto.name());
        }

        category.setName(dto.name());
        category.setDescription(dto.description());

        return categoryMapper.toDto(categoryRepositoryPort.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        String categoryName = category.getName();

        if (!productRepositoryPort.existsAnyByCategoryId(id)) {
            categoryRepositoryPort.delete(category.getId());
        } else {
            throw new CategoryNotEmpty(categoryName);
        }
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepositoryPort.findByName(name).orElseThrow(
                () -> new CategoryNotFoundException(name)
        );
        return categoryMapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );
        return categoryMapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        return categoryRepositoryPort.findAll(sorted).map(categoryMapper::toDto);
    }
}
