package by.may.mapper;

import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.entity.Category;
import by.may.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest dto, Category category) {
        return new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                category
        );
    }

    public ProductResponse toDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCreatedAt()
        );
    }
}
