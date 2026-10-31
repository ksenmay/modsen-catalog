package by.may.application.mapper;

import by.may.application.dto.ProductCreateRequest;
import by.may.application.dto.ProductResponse;
import by.may.domain.model.Product;
import by.may.infrastructure.entity.CategoryJpaEntity;
import by.may.infrastructure.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toModel(ProductCreateRequest dto) {
        return new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.categoryId()
        );
    }

    public ProductResponse toDto(Product model) {
        return new ProductResponse(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                model.getCategoryId(),
                model.getCreatedAt()
        );
    }
}
