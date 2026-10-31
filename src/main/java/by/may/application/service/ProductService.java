package by.may.application.service;

import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;
import by.may.application.mapper.PriceHistoryMapper;
import by.may.application.port.PriceHistoryRepositoryPort;
import by.may.application.port.ProductRepositoryPort;
import by.may.application.port.ReviewRepositoryPort;
import by.may.application.service.command.ProductCommand;
import by.may.application.service.query.ProductQuery;
import by.may.domain.model.PriceHistory;
import by.may.domain.model.Product;
import by.may.application.mapper.ProductMapper;
import by.may.domain.exception.ProductNotFoundException;
import by.may.infrastructure.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductCommand, ProductQuery {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductMapper productMapper;
    private final PriceHistoryRepositoryPort priceHistoryRepositoryPort;
    private final ReviewRepositoryPort reviewRepositoryPort;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest dto) {
        Product product = productMapper.toModel(dto);
        return productMapper.toDto(productRepositoryPort.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductCreateRequest dto) {
        Product product = productRepositoryPort.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        product.setName(dto.name());
        product.setCategoryId(dto.categoryId());
        product.setDescription(dto.description());

        BigDecimal oldPrice = product.getPrice();
        BigDecimal newPrice = dto.price();

        boolean priceChanged = newPrice.compareTo(oldPrice) != 0;

        product.setPrice(newPrice);

        if (priceChanged) {
            PriceHistory priceHistory = new PriceHistory(id, oldPrice, newPrice);
            priceHistoryRepositoryPort.save(priceHistory);
        }

        return productMapper.toDto(productRepositoryPort.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepositoryPort.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        reviewRepositoryPort.softDeleteByProductId(id);
        priceHistoryRepositoryPort.softDeleteByProductId(id);
        productRepositoryPort.delete(product.getId());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepositoryPort.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(ProductSearchCriteria criteria, Pageable pageable) {
        return productRepositoryPort.findAll(criteria, pageable).map(productMapper::toDto);
    }
}
