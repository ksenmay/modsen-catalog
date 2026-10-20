package by.may.exception;

public class ReviewNotWrittenByUserException extends RuntimeException {
    public ReviewNotWrittenByUserException(Long userId, Long reviewId) {
        super("User with id = " + userId + " not write review with id = " + reviewId);
    }
}
