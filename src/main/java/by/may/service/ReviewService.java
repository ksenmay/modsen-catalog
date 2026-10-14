package by.may.service;

import by.may.entity.Review;
import by.may.exception.ReviewNotFoundException;
import by.may.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public double calculateAverageRating(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews.stream().mapToDouble(Review::getRating).average().orElseThrow(
                () -> new ReviewNotFoundException(productId)
        );
    }
}
