package by.may.infrastructure.repository;

import by.may.infrastructure.entity.ReviewJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewJpaEntity, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Page<ReviewJpaEntity> findAllByProductId(Long productId, Pageable pageable);

    @Query("select coalesce(avg(r.rating), 0.0) from Review r where r.product.id = :productId")
    double calculateAverageRating(@Param("productId") Long productId);

    @Modifying(clearAutomatically = true)
    @Query("update Review r set r.deleted = true where r.product.id =:productId")
    void softDeleteByProductId(@Param("productId") Long productId);
}
