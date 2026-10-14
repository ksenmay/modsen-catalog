package by.may.repository;

import by.may.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    Page<Product> findByName(Pageable name);

    List<Product> findAllByCategoryId(Long categoryId);
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findAllByPriceBetween(double min, double max);
    Page<Product> findAllByPriceBetween(double min, double max, Pageable pageable);

    Optional<Product> getProductById(Long id);

}
