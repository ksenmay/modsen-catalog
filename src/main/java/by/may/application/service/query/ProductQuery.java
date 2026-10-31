package by.may.application.service.query;

import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;
import by.may.application.service.ProductSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQuery {
    ProductResponse findById(Long id);
    Page<ProductResponse> searchProducts(ProductSearchCriteria criteria, Pageable pageable);
}
