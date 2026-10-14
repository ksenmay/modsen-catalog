package by.may.exception;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(Long id) {
        super("Category with " + id + " not found");
    }
}