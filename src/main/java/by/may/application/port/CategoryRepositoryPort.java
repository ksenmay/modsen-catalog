package by.may.application.port;

import by.may.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepositoryPort {
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long id);
    boolean existsByName(String name);
    Category save(Category category);
    void delete(Long id);
    Page<Category> findAll(Pageable pageable);
}