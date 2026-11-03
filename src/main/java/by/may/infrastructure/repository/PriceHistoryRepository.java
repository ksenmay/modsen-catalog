package by.may.infrastructure.repository;

import by.may.infrastructure.entity.PriceHistoryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistoryJpaEntity, Long> {
    Page<PriceHistoryJpaEntity> findAllByProductId(Long id, Pageable pageable);
}
