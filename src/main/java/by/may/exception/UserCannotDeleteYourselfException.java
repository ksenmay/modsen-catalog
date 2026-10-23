package by.may.exception;

public class UserCannotDeleteYourselfException extends RuntimeException {
    public UserCannotDeleteYourselfException() {
        super("You cannot delete yourself");
    }
}
