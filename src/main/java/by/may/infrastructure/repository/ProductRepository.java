package by.may.infrastructure.repository;

import by.may.infrastructure.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductJpaEntity, Long>, JpaSpecificationExecutor<ProductJpaEntity> {
    Optional<ProductJpaEntity> findById(Long id);
    @Query(value = "select count(*) > 0 from products where category_id = :categoryId", nativeQuery = true)
    boolean existsAnyByCategoryId(@Param("categoryId") Long categoryId);
}