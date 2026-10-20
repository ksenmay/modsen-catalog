package by.may.exception;

public class AdminMinLimitException extends RuntimeException {
    public AdminMinLimitException() {
        super("Cannot remove the last administrator");
    }
}
