package by.may.application.service;

import by.may.application.dto.ReviewCreateRequest;
import by.may.application.port.EventPublisher;
import by.may.application.port.ProductRepositoryPort;
import by.may.application.port.ReviewRepositoryPort;
import by.may.application.port.UserRepositoryPort;
import by.may.domain.Role;
import by.may.domain.exception.ProductNotFoundException;
import by.may.domain.exception.ReviewNotWrittenByUserException;
import by.may.domain.exception.UserAlreadyAddReviewForProduct;
import by.may.domain.exception.UserNotFoundException;
import by.may.domain.model.Product;
import by.may.domain.model.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepositoryPort reviewRepositoryPort;
    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private EventPublisher publisher;

    @InjectMocks private ReviewService reviewService;

    @Test
    void create_throwsWhenProductNotFound() {
        ReviewCreateRequest dto = new ReviewCreateRequest(5, "Отлично", 1L, 77L);
        when(userRepositoryPort.existsById(1L)).thenReturn(true);
        when(productRepositoryPort.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> reviewService.createReview(dto, 1L));
    }

    @Test
    void create_throwsWhenUserAlreadyReviewedProduct() {
        ReviewCreateRequest dto = new ReviewCreateRequest(5, "Отлично", 1L, 10L);
        when(userRepositoryPort.existsById(1L)).thenReturn(true);
        when(productRepositoryPort.findById(10L))
                .thenReturn(Optional.of(new Product(10L, "Ноутбук", "d", BigDecimal.TEN, 1L, LocalDate.now(), 0.0)));
        when(reviewRepositoryPort.existsByUserIdAndProductId(1L, 10L)).thenReturn(true);

        assertThrows(UserAlreadyAddReviewForProduct.class,
                () -> reviewService.createReview(dto, 1L));
        verify(reviewRepositoryPort, never()).save(any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void create_throwsWhenUserNotFound() {
        ReviewCreateRequest dto = new ReviewCreateRequest(5, "Отлично", 10L, 1L);
        when(userRepositoryPort.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> reviewService.createReview(dto, 99L));
    }

    @Test
    void update_throwsWhenReviewBelongsToAnotherUser() {
        Review review = new Review(5L, 4, "Чужой", 4L, 10L, LocalDate.now());
        when(reviewRepositoryPort.findById(5L)).thenReturn(Optional.of(review));
        ReviewCreateRequest dto = new ReviewCreateRequest(1, "хочу правку", 10L, 1L);

        assertThrows(ReviewNotWrittenByUserException.class,
                () -> reviewService.updateReview(5L, 1L, dto));
        verify(reviewRepositoryPort, never()).save(any());
    }

    @Test
    void delete_throwsForNonAdminDeletingOtherUsersReview() {
        Review review = new Review(5L, 4, "Чужлй", 4L, 1L, LocalDate.now());
        when(reviewRepositoryPort.findById(5L)).thenReturn(Optional.of(review));

        assertThrows(ReviewNotWrittenByUserException.class,
                () -> reviewService.deleteReview(5L, 1L, Role.CUSTOMER));
        verify(reviewRepositoryPort, never()).delete(any());
    }
}