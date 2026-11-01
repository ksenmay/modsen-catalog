package by.may.application.port;

import by.may.domain.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewRepositoryPort {
    Review save(Review review);
    void delete(Long id);
    Optional<Review> findById(Long id);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void softDeleteByProductId(Long productId);
    Page<Review> findAllByProductId(Long productId, Pageable pageable);
    double calculateAverageRating(Long productId);

}
