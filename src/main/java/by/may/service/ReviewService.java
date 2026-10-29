package by.may.service;

import by.may.auth.SessionContext;
import by.may.dto.ReviewCreateRequest;
import by.may.dto.ReviewResponse;
import by.may.entity.Role;
import by.may.event.ReviewAddedEvent;
import by.may.event.ReviewDeletedEvent;
import by.may.mapper.ReviewMapper;
import by.may.entity.Product;
import by.may.entity.Review;
import by.may.entity.User;
import by.may.exception.*;
import by.may.repository.ProductRepository;
import by.may.repository.ReviewRepository;
import by.may.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SessionContext sessionContext;

    @Transactional
    public ReviewResponse createReview(Long productId, Long userId, ReviewCreateRequest dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );

        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new UserAlreadyAddReviewForProduct(userId, productId);
        }

        Review review = reviewMapper.toEntity(dto, user, product);
        reviewRepository.save(review);

        applicationEventPublisher.publishEvent(new ReviewAddedEvent(user.getUsername()));

        product.setAverageRating(reviewRepository.calculateAverageRating(productId));

        return reviewMapper.toDto(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, Long userId, ReviewCreateRequest dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );

        if (!Objects.equals(review.getUser().getId(), userId)) {
            throw new ReviewNotWrittenByUserException(userId, reviewId);
        }

        review.setRating(dto.rating());
        review.setComment(dto.comment());

        Product product = review.getProduct();

        product.setAverageRating(reviewRepository.calculateAverageRating(product.getId()));
        return reviewMapper.toDto(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long currentUserId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );

        User currentUser = userRepository.findById(currentUserId).orElseThrow(
                () -> new UserNotFoundException(currentUserId)
        );

        if (review.getUser() == currentUser || sessionContext.getCurrentUserRole().equals(Role.ADMIN)) {
            throw new ReviewNotWrittenByUserException(reviewId, currentUser.getId());
        }

        applicationEventPublisher.publishEvent(new ReviewDeletedEvent(currentUser.getUsername()));

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        return reviewRepository.findAllByProductId(productId, pageable)
                .map(reviewMapper::toDto);
    }

}
