package by.may.presentation.console;

import by.may.dto.CategoryResponse;
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

    public static void printUserPage(Page<UserResponse> page) {
        page.getContent().forEach(u ->
                System.out.println(u.id() + ". " + u.username() + " - " + u.email() + " (" + u.role() + ")"));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }

    public static void printCategoryPage(Page<CategoryResponse> page) {
        page.getContent().forEach(c ->
                System.out.println(c.id() + ". " + c.name() + " - " + c.description()));
        System.out.println("Страница " + (page.getNumber() + 1) + " из " + Math.max(page.getTotalPages(), 1));
    }

}
