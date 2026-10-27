package by.may.exception;

public class UserAlreadyAddReviewForProduct extends RuntimeException {
    public UserAlreadyAddReviewForProduct(Long userId, Long productId) {
        super("User with id = " + userId + " already add review for product with id = " + productId);
    }
}
