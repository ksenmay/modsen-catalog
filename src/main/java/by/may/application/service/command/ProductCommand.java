package by.may.application.service.command;

import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;

public interface ProductCommand {
    ProductResponse createProduct(ProductCreateRequest dto);
    ProductResponse updateProduct(Long id, ProductCreateRequest dto);
    void deleteProduct(Long id);
}
