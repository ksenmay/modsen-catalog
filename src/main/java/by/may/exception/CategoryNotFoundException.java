package by.may.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category with " + id + " not found");
    }

    public CategoryNotFoundException(String name) {
        super("Category with " + name + " not found");
    }
}