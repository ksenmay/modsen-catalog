package by.may.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String param, String value) {
        super("User with " + param + " '" + value + "' already exists");
    }
}
