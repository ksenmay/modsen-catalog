package by.may.application.service.query;

import by.may.application.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewQuery {
    Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable);
}
