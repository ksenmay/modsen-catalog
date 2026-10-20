package by.may.service;

import by.may.dto.ReviewCreateRequest;
import by.may.dto.ReviewResponse;
import by.may.mapper.ReviewMapper;
import by.may.entity.Product;
import by.may.entity.Review;
import by.may.entity.User;
import by.may.exception.*;
import by.may.repository.ProductRepository;
import by.may.repository.ReviewRepository;
import by.may.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    //TODO в этом метод надо будет пересмотреть получение текущего пользователя
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
        refreshAverageRating(productId);

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

        refreshAverageRating(product.getId());
        return reviewMapper.toDto(review);
    }

    public double calculateAverageRating(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

//    public void deleteReview(Long reviewId) {
//
//    }
//
//    private Review findOwnReview() {
//
//    }

    private double refreshAverageRating(Long productId) {
        return 0.0;
    }

}
