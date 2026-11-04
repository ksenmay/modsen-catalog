package by.may.web.controllers;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.dto.ReviewResponse;
import by.may.application.service.ReviewService;
import by.may.domain.Role;
import by.may.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Отзывы", description = "Отзывы на продукты")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Отзывы продукта",
            description = "Постраничный список отзывов")
    @SecurityRequirements
    @GetMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Page<ReviewResponse> getByProduct (@PathVariable Long productId,
                                              @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return reviewService.getReviewsByProduct(productId, pageable);
    }

    @Operation(summary = "Оставить отзыв",
            description = "Доступно авторизованным пользователям, один отзыв на продукт от одного пользователя.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create (@Valid @RequestBody ReviewCreateRequest dto,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        return reviewService.createReview(dto, currentUser.id());
    }

    @Operation(summary = "Изменить свой отзыв",
            description = "Доступно автору отзыва")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/reviews/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponse update (@PathVariable Long reviewId,
                                  @Valid @RequestBody ReviewCreateRequest dto,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        return reviewService.updateReview(reviewId, currentUser.id(), dto);
    }

    @Operation(summary = "Удалить свой отзыв",
            description = "Доступно автору отзыва и ADMIN")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/api/reviews/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long productId,
                        @AuthenticationPrincipal CurrentUser currentUser,
                        Role userRole) {
        reviewService.deleteReview(productId, currentUser.id(), userRole);
    }

}
