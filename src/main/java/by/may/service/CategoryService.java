package by.may.service;

import by.may.dto.CategoryCreateRequest;
import by.may.dto.CategoryResponse;
import by.may.event.CategoryDeletedEvent;
import by.may.exception.CategoryNotEmpty;
import by.may.mapper.CategoryMapper;
import by.may.entity.Category;
import by.may.exception.CategoryAlreadyExistsException;
import by.may.exception.CategoryNotFoundException;
import by.may.repository.CategoryRepository;
import by.may.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProductRepository productRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest dto) {
        if (categoryRepository.existsByName(dto.name())) {
            throw new CategoryAlreadyExistsException(dto.name());
        }
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryCreateRequest dto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        if (!category.getName().equals(dto.name()) && categoryRepository.existsByName(dto.name())) {
            throw new CategoryAlreadyExistsException(dto.name());
        }

        category.setName(dto.name());
        category.setDescription(dto.description());
        return categoryMapper.toDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        String categoryName = category.getName();

        if (!categoryIsEmpty(id)) {
            throw new CategoryNotEmpty(categoryName);
        }

        categoryRepository.delete(category);
        applicationEventPublisher.publishEvent(new CategoryDeletedEvent(categoryName));
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(
                () -> new CategoryNotFoundException(name)
        );
        return categoryMapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    private boolean categoryIsEmpty(Long categoryId) {
        return productRepository.existsAnyByCategoryId(categoryId);
    }

}
