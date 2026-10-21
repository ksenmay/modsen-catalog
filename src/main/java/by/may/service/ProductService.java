package by.may.service;

import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.entity.Category;
import by.may.exception.CategoryNotFoundException;
import by.may.mapper.ProductMapper;
import by.may.entity.Product;
import by.may.entity.Review;
import by.may.exception.ProductNotFoundException;
import by.may.repository.CategoryRepository;
import by.may.repository.ProductRepository;
import by.may.repository.ProductSpecifications;
import by.may.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService { //TODO добавить events

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest dto) { //TODO пересмотреть как будет взаимодействовать с категориями
        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(
                () -> new CategoryNotFoundException(dto.categoryId())
        );

        Product product = productMapper.toEntity(dto, category);
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductCreateRequest dto) { //TODO после добавления сущности PriceHistory обновить метод
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(
                () -> new CategoryNotFoundException(dto.categoryId())
        );

        product.setName(dto.name());
        product.setCategory(category);
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        return productMapper.toDto(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        reviewRepository.findAllByProductId(id).forEach(Review::markDelete);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(ProductSearchCriteria productSearchCriteria, Pageable pageable) {
        Specification<Product> specification = Specification
                .where(ProductSpecifications.nameContains(productSearchCriteria.name())
                        .and(ProductSpecifications.inCategory(productSearchCriteria.categoryId()))
                        .and(ProductSpecifications.priceBetween(productSearchCriteria.minPrice(), productSearchCriteria.maxPrice()))
                        .and(ProductSpecifications.minRating(productSearchCriteria.minRating())));

        PageRequest request = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                buildSort(productSearchCriteria)
        );
        return productRepository.findAll(specification, request)
                .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    private Sort buildSort(ProductSearchCriteria productSearchCriteria) {
        if (productSearchCriteria.sortBy() == null) {
            return Sort.by("id");
        }
        Sort.Direction direction;
        if (productSearchCriteria.asc()) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, productSearchCriteria.sortBy().getField());
    }
}
