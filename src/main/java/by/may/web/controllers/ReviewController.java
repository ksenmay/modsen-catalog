package by.may.web.controllers;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.application.service.ReviewService;
import by.may.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Page<ReviewResponse> getByProduct (@PathVariable Long productId,
                                              @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return reviewService.getReviewsByProduct(productId, pageable);
    }

    @PostMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create (@PathVariable Long productId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  Long userId) {
        return reviewService.createReview(productId, userId, dto);
    }

    @PutMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponse update (@PathVariable Long productId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  Long userId) {
        return reviewService.updateReview(productId, userId, dto);
    }

    @DeleteMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long productId,
                        Long userId, Role userRole) {
        reviewService.deleteReview(productId, userId, userRole);
    }

}
