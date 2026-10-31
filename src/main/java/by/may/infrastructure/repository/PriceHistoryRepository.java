package by.may.infrastructure.repository;

import by.may.infrastructure.entity.PriceHistoryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryJpaEntity, Long> {
    Page<PriceHistoryJpaEntity> findAllByProductId(Long id);
    @Modifying(clearAutomatically = true)
    @Query("update PriceHistory pr set pr.deleted = true where pr.product.id =:productId")
    void softDeleteByProductId(@Param("productId") Long productId);
}
