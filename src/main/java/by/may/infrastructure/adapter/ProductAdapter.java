package by.may.infrastructure.adapter;

import by.may.application.port.ProductRepositoryPort;
import by.may.application.service.ProductSearchCriteria;
import by.may.domain.exception.CategoryNotFoundException;
import by.may.domain.exception.ProductNotFoundException;
import by.may.domain.model.Product;
import by.may.infrastructure.entity.CategoryJpaEntity;
import by.may.infrastructure.entity.ProductJpaEntity;
import by.may.infrastructure.mapper.ProductJpaMapper;
import by.may.infrastructure.repository.CategoryRepository;
import by.may.infrastructure.repository.ProductRepository;
import by.may.infrastructure.repository.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepositoryPort {

    private final ProductRepository repository;
    private final ProductJpaMapper jpaMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(jpaMapper::toModel);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsAnyByCategoryId(Long categoryId) {
        return repository.existsAnyByCategoryId(categoryId);
    }

    @Override
    public Product save(Product product) {
        CategoryJpaEntity categoryJpa = categoryRepository.findById(product.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException(product.getCategoryId())
        );
        return jpaMapper.toModel(repository.save(jpaMapper.toNewEntity(product, categoryJpa)));
    }

    @Override
    public void delete(Long id) {
        ProductJpaEntity entity = repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        repository.delete(entity);
    }

    @Override
    public Page<Product> findAll(ProductSearchCriteria criteria, Pageable pageable) {
        Specification<ProductJpaEntity> specification = Specification
                .where(ProductSpecifications.nameContains(criteria.name()))
                .and(ProductSpecifications.inCategory(criteria.categoryId()))
                .and(ProductSpecifications.priceBetween(criteria.minPrice(), criteria.maxPrice()))
                .and(ProductSpecifications.minRating(criteria.minRating()));

        PageRequest request = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                buildSort(criteria)
        );
       return repository.findAll(specification, request).map(jpaMapper::toModel);
    }

    private Sort buildSort(ProductSearchCriteria criteria) {
        if (criteria.sortBy() == null) {
            return Sort.by("id");
        }
        Sort.Direction direction;
        if (criteria.asc()) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, criteria.sortBy().getField());
    }

}
