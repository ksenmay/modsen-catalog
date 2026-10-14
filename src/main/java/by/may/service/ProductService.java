package by.may.service;

import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.dto.mapper.ProductMapper;
import by.may.entity.Product;
import by.may.entity.Review;
import by.may.exception.ProductNotFoundException;
import by.may.repository.ProductRepository;
import by.may.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService { //TODO добавить events

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ReviewRepository reviewRepository;

    public ProductResponse createProduct(ProductCreateRequest dto) { //TODO пересмотреть как будет взаимодействовать с категориями
        Product product = productMapper.toEntity(dto);
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductCreateRequest dto) { //TODO после добавления сущности PriceHistory обновить метод
        Product product = productRepository.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        product.setName(dto.name());
        product.setCategory(dto.category());
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        return productMapper.toDto(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        reviewRepository.findAllByProductId(id).forEach(Review::markDelete);
        productRepository.delete(product);
    }

    ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        return productMapper.toDto(product);
    }

    //TODO дописать пагинацию, поиск, фильтры, сортировку
}
