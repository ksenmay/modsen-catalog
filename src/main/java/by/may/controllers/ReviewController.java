package by.may.controllers;

import by.may.dto.ReviewCreateRequest;
import by.may.dto.ReviewResponse;
import by.may.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/products/{productId}/reviews")
    public Page<ReviewResponse> getByProduct (@PathVariable Long productId,
                                              @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return reviewService.getReviewsByProduct(productId, pageable);
    }

    @PostMapping("/api/products/{productId}/reviews")
    public ReviewResponse create (@PathVariable Long productId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  Long userId) {
        return reviewService.createReview(productId, userId, dto);
    }

    @PutMapping("/api/products/{productId}/reviews")
    public ReviewResponse update (@PathVariable Long productId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  Long userId) {
        return reviewService.updateReview(productId, userId, dto);
    }

    @DeleteMapping("/api/products/{productId}/reviews")
    public void delete (@PathVariable Long productId,
                                  Long userId) {
        reviewService.deleteReview(productId, userId);
    }

}
