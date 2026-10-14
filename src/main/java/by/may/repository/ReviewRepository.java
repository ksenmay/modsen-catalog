package by.may.repository;

import by.may.entity.Review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    List<Review> findAllByProductId(Long productId);
    Page<Review> findAllByProductId(Long productId, Pageable pageable);
}
