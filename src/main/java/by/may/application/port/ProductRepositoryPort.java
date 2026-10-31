package by.may.application.port;

import by.may.application.service.ProductSearchCriteria;
import by.may.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface ProductRepositoryPort {
    Optional<Product> findById(Long id);
    boolean existsById(Long id);
    boolean existsAnyByCategoryId(Long categoryId);
    Product save(Product product);
    void delete(Long id);
    Page<Product> findAll(ProductSearchCriteria criteria, Pageable pageable);
}
