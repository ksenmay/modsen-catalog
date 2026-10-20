package by.may.service;

import by.may.dto.CategoryCreateRequest;
import by.may.dto.CategoryResponse;
import by.may.mapper.CategoryMapper;
import by.may.entity.Category;
import by.may.exception.CategoryAlreadyExistsException;
import by.may.exception.CategoryNotFoundException;
import by.may.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

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

        //TODO написать проверку для продуктов, которые лежат в категории

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name")).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

}
