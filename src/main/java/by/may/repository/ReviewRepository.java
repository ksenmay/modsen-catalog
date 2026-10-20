package by.may.repository;

import by.may.entity.Review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Page<Review> findAllByProductId(Long productId, Pageable pageable);
    List<Review> findAllByProductId(Long productId);

    @Query("select coalesce(avg(r.rating), 0.0) from Review r where r.product.id = :productId")
    double calculateAverageRating(@Param("productId") Long productId);

    @Query("update Review r set r.deleted = true where r.product.id =: productId") //TODO пересмотреть
    int softDeleteByProductId(@Param("productId") Long productId);
}
