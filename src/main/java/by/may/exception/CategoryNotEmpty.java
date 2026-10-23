package by.may.exception;

public class CategoryNotEmpty extends RuntimeException {
    public CategoryNotEmpty(String categoryName) {
        super("Category " + categoryName + " is not empty");
    }
}
