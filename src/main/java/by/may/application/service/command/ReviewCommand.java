package by.may.application.service.command;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.domain.Role;

public interface ReviewCommand {
    ReviewResponse createReview(Long productId, Long userId, ReviewCreateRequest dto);
    ReviewResponse updateReview(Long reviewId, Long userId, ReviewCreateRequest dto);
    void deleteReview(Long reviewId, Long currentUserId, Role currentUserRole);
}
