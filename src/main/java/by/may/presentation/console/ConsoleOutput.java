package by.may.presentation.console;

import by.may.dto.CategoryResponse;
import by.may.dto.ProductResponse;
import by.may.dto.ReviewResponse;
import by.may.dto.UserResponse;
import org.springframework.data.domain.Page;

public final class ConsoleOutput {

    public static void success(String text) {
        System.out.println(Colors.GREEN + text + Colors.RESET);
    }

    public static void error(String text) {
        System.out.println(Colors.RED + text + Colors.RESET);
    }

    public static void info(String text) {
        System.out.println(Colors.YELLOW + text + Colors.RESET);
    }

    public static void title(String text) {
        System.out.println(Colors.BLUE + text + Colors.RESET);
    }

    public static void printProduct(ProductResponse response) {
        ConsoleOutput.info("Название продукта: " + response.name());
        ConsoleOutput.info("Описание: " + response.description());
        ConsoleOutput.info("Цена" + response.price());
        ConsoleOutput.info("Категория (ID): " + response.categoryId());
    }

    public static void printUserPage(Page<UserResponse> page) {
        page.getContent().forEach(u ->
                System.out.println(u.id() + ". " + u.username() + " - " + u.email() + " (" + u.role() + ")"));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }

    public static void printProductPage(Page<ProductResponse> page) {
        page.getContent().forEach(p ->
                System.out.println(p.id() + ". " + p.name() + " - "
                        + p.description() + " - "
                        + p.categoryId() + " - "
                        + p.price() + " - "
                        + p.createdAt()));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }


    public static void printCategoryPage(Page<CategoryResponse> page) {
        page.getContent().forEach(c ->
                System.out.println(c.id() + ". " + c.name() + " - " + c.description()));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }

    public static void printReviewPage(Page<ReviewResponse> page) {
        page.getContent().forEach(r ->
                System.out.println(r.id() + ". [" + r.rating() + "/5] " + r.userId() + ": " + r.comment()));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }

}
