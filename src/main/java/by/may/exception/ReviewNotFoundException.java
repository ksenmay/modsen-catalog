package by.may.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long productId) {
        super("Reviews for product id: " + productId + " not found");
    }
}
