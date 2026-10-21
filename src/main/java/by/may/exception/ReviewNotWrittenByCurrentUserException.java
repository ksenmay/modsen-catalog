package by.may.exception;

public class ReviewNotWrittenByCurrentUserException extends RuntimeException {
    public ReviewNotWrittenByCurrentUserException(Long reviewId, Long userId) {
        super("Review with id = " + reviewId + " not written by user with id = " + userId);
    }
}
