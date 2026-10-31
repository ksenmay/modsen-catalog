package by.may.infrastructure.mapper;

import by.may.domain.model.Product;
import by.may.infrastructure.entity.CategoryJpaEntity;
import by.may.infrastructure.entity.ProductJpaEntity;

public class ProductJpaMapper {

    public Product toModel(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategoryJpaEntity().getId(),
                entity.getCreatedAt(),
                entity.getAverageRating()
        );
    }

    public ProductJpaEntity toNewEntity(Product model, CategoryJpaEntity categoryJpa) {
        return new ProductJpaEntity(
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                categoryJpa
        );
    }

    public ProductJpaEntity updateEntity(Product model, ProductJpaEntity entity, CategoryJpaEntity categoryJpa) {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        entity.setCategoryJpaEntity(categoryJpa);
        return entity;
    }

    public ProductJpaEntity updateEntity(Product model, ProductJpaEntity entity) {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        return entity;
    }

}
