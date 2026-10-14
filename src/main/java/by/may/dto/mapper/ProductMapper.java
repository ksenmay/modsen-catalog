package by.may.dto.mapper;

import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest dto) {
        return new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category()
        );
    }

    public ProductResponse toDto(Product product) {
        return new ProductResponse(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCreatedAt()
        );
    }
}
