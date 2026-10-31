package by.may.infrastructure.mapper;

import by.may.domain.model.Category;
import by.may.infrastructure.entity.CategoryJpaEntity;

public class CategoryJpaMapper {
    public Category toModel(CategoryJpaEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getDescription());
    }

    public CategoryJpaEntity toNewEntity(Category model) {
        return new CategoryJpaEntity(
                model.getName(),
                model.getDescription()
        );
    }

    public CategoryJpaEntity updateEntity(CategoryJpaEntity entity, Category model) {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        return entity;
    }
}
