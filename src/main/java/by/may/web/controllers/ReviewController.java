package by.may.web.controllers;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.application.service.ReviewService;
import by.may.domain.Role;
import by.may.infrastructure.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create (@Valid @RequestBody ReviewCreateRequest dto,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        return reviewService.createReview(dto, currentUser.id());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponse update (@PathVariable Long productId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        return reviewService.updateReview(productId, currentUser.id(), dto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/api/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long productId,
                        @AuthenticationPrincipal CurrentUser currentUser,
                        Role userRole) {
        reviewService.deleteReview(productId, currentUser.id(), userRole);
    }

}
