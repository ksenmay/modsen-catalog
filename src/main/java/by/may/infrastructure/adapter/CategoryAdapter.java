package by.may.infrastructure.adapter;

import by.may.application.port.CategoryRepositoryPort;
import by.may.domain.exception.CategoryNotFoundException;
import by.may.domain.model.Category;
import by.may.infrastructure.entity.CategoryJpaEntity;
import by.may.infrastructure.mapper.CategoryJpaMapper;
import by.may.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository repository;
    private final CategoryJpaMapper jpaMapper;

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name).map(jpaMapper::toModel);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repository.findById(id).map(jpaMapper::toModel);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity;
        if (category.getId() == null) {
            entity = jpaMapper.toNewEntity(category);
        } else {
            CategoryJpaEntity existing = repository.findById(category.getId()).orElseThrow(
                    () -> new CategoryNotFoundException(category.getId())
            );
            entity = jpaMapper.updateEntity(existing, category);
        }
        return jpaMapper.toModel(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CategoryJpaEntity entity = repository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );
        repository.delete(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(jpaMapper::toModel);
    }
}
