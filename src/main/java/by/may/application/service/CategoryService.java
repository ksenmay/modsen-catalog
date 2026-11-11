package by.may.application.service;

import by.may.application.dto.CategoryCreateRequest;
import by.may.application.dto.CategoryResponse;
import by.may.application.mapper.CategoryMapper;
import by.may.application.port.CategoryRepositoryPort;
import by.may.application.port.EventPublisher;
import by.may.application.port.ProductRepositoryPort;
import by.may.application.service.command.CategoryCommand;
import by.may.application.service.query.CategoryQuery;
import by.may.domain.event.CategoryDeletedEvent;
import by.may.domain.exception.CategoryAlreadyExistsException;
import by.may.domain.exception.CategoryNotEmpty;
import by.may.domain.exception.CategoryNotFoundException;
import by.may.domain.model.Category;
import by.may.infrastructure.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements CategoryCommand, CategoryQuery {

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final CategoryMapper categoryMapper;
    private final ProductRepositoryPort productRepositoryPort;
    private final EventPublisher publisher;

    @CacheEvict(cacheNames = {
            CacheConfig.CATEGORIES_LIST,
            CacheConfig.CATEGORY_BY_ID,
            CacheConfig.CATEGORY_BY_NAME
    }, allEntries = true)
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest dto) {
        if (categoryRepositoryPort.existsByName(dto.name())) {
            log.error("категория с {} уже существует", dto.name());
            throw new CategoryAlreadyExistsException(dto.name());
        }
        Category category = categoryMapper.toModel(dto);
        log.info("создана категория {}", dto.name());
        return categoryMapper.toDto(categoryRepositoryPort.save(category));
    }

    @CacheEvict(cacheNames = {
            CacheConfig.CATEGORIES_LIST,
            CacheConfig.CATEGORY_BY_ID,
            CacheConfig.CATEGORY_BY_NAME
    }, allEntries = true)
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryCreateRequest dto) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        if (!category.getName().equals(dto.name()) && categoryRepositoryPort.existsByName(dto.name())) {
            log.error("нельзя поменять название категории {}, так как категория с {} уже существует",
                    category.getName(), dto.name());
            throw new CategoryAlreadyExistsException(dto.name());
        }

        category.setName(dto.name());
        category.setDescription(dto.description());

        log.info("изменена категория {}, {}", id, category.getName());
        return categoryMapper.toDto(categoryRepositoryPort.save(category));
    }

    @CacheEvict(cacheNames = {
            CacheConfig.CATEGORIES_LIST,
            CacheConfig.CATEGORY_BY_ID,
            CacheConfig.CATEGORY_BY_NAME
    }, allEntries = true)
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );

        String categoryName = category.getName();

        if (!productRepositoryPort.existsAnyByCategoryId(id)) {
            categoryRepositoryPort.delete(category.getId());
            publisher.publish(new CategoryDeletedEvent(categoryName));
            log.info("удалена категория {}, {}", id, category.getName());
        } else {
            log.error("Категория {}, {} не удалена, так как не пуста", id, category.getName());
            throw new CategoryNotEmpty(categoryName);
        }
    }

    @Cacheable(cacheNames = CacheConfig.CATEGORY_BY_ID, key = "#id")
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepositoryPort.findByName(name).orElseThrow(
                () -> new CategoryNotFoundException(name)
        );
        return categoryMapper.toDto(category);
    }

    @Cacheable(cacheNames = CacheConfig.CATEGORY_BY_NAME, key = "#name")
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepositoryPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );
        return categoryMapper.toDto(category);
    }

    @Cacheable(cacheNames = CacheConfig.CATEGORIES_LIST, key = "#pageable")
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        return categoryRepositoryPort.findAll(sorted).map(categoryMapper::toDto);
    }
}
