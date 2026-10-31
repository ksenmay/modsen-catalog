package by.may.application.service;

import by.may.application.port.ProductRepositoryPort;
import by.may.application.port.ReviewRepositoryPort;
import by.may.application.port.UserRepositoryPort;
import by.may.application.service.command.ReviewCommand;
import by.may.application.service.query.ReviewQuery;
import by.may.domain.exception.*;
import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.domain.Role;
import by.may.application.mapper.ReviewMapper;
import by.may.domain.model.Product;
import by.may.domain.model.Review;
import by.may.infrastructure.event.EventPublisher;
import by.may.infrastructure.event.ReviewAddedEvent;
import by.may.infrastructure.event.ReviewDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements ReviewCommand, ReviewQuery {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ReviewMapper reviewMapper;
    private final EventPublisher publisher;

    @Transactional
    public ReviewResponse createReview(Long productId, Long userId, ReviewCreateRequest dto) {
        if (!userRepositoryPort.existsById(userId)) {
            log.error("пользователь {} не найден", userId);
            throw new UserNotFoundException(userId);
        }

        Product product = productRepositoryPort.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );

        if (reviewRepositoryPort.existsByUserIdAndProductId(userId, productId)) {
            log.error("пользователь {} уже писал отзыв на продукт {}",
                    userId, productId);
            throw new UserAlreadyAddReviewForProduct(userId, productId);
        }

        product.setAverageRating(reviewRepositoryPort.calculateAverageRating(productId));
        publisher.publish(new ReviewAddedEvent());
        log.info("создан отзыв на продукт {}", product.getName());
        return reviewMapper.toDto(reviewRepositoryPort.save(reviewMapper.toModel(dto)));
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, Long userId, ReviewCreateRequest dto) {
        Review review = reviewRepositoryPort.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );

        if (!review.getUserId().equals(userId)) {
            log.error("отзыв {} на продукт {} не написан пользователем {}",
                    reviewId, review.getProductId(), userId);
            throw new ReviewNotWrittenByUserException(userId, reviewId);
        }

        review.setRating(dto.rating());
        review.setComment(dto.comment());

        Product product = productRepositoryPort.findById(review.getProductId()).orElseThrow(
                () -> new ProductNotFoundException(review.getProductId())
        );

        product.setAverageRating(reviewRepositoryPort.calculateAverageRating(product.getId()));
        log.info("измененён отзыв {} на продукт {}", review.getId(), product.getName());
        return reviewMapper.toDto(reviewRepositoryPort.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId, Long currentUserId, Role currentUserRole) {
        Review review = reviewRepositoryPort.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );

        if (!review.getUserId().equals(currentUserId) || currentUserRole != Role.ADMIN) {
            log.error("отзыв {} на продукт {} не написан пользователем {} или администратором",
                    reviewId, review.getProductId(), currentUserId);
            throw new ReviewNotWrittenByUserException(reviewId, currentUserId);
        }

        reviewRepositoryPort.delete(reviewId);
        publisher.publish(new ReviewDeletedEvent());
        log.info("удалён отзыв {} на продукт {}", review.getId(), review.getProductId());
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable) {
        if (!productRepositoryPort.existsById(productId)) {
            log.error("продукт {} не существует", productId);
            throw new ProductNotFoundException(productId);
        }
        return reviewRepositoryPort.findAllByProductId(productId, pageable)
                .map(reviewMapper::toDto);
    }
}
