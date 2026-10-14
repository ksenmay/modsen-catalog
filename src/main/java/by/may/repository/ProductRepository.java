package by.may.repository;

import by.may.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> { //TODO добавить сортировку
    Page<Product> findByNameContainingIgnoreCase(Pageable name);
    List<Product> findByNameContainingIgnoreCase(String name);

    boolean existsByCategoryId(Long categoryId);

    Optional<Product> getProductById(Long id);
}