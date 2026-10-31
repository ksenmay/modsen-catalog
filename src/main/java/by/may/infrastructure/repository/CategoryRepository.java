package by.may.infrastructure.repository;

import by.may.infrastructure.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findByName(String name);
    Optional<CategoryJpaEntity> findById(Long id);
    boolean existsByName(String name);
}
