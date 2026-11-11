package by.may.domain.exception;

public class AdminMinLimitException extends RuntimeException {
    public AdminMinLimitException() {
        super("Cannot remove the last administrator");
    }
}
