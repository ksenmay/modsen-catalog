package by.may.presentation.command.categories;

import by.may.auth.SessionContext;
import by.may.dto.CategoryResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ListCategoriesCommand implements Command {

    private final SessionContext sessionContext;
    private final CategoryService categoryService;
    private final ConsoleInput consoleInput;

    @Override
    public String title() {
        return "Просмотр категорий";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        int page = 0;
        while (true) {
            Page<CategoryResponse> responses = categoryService.getCategories(PageRequest.of(page, 10));
            ConsoleOutput.printCategoryPage(responses);

            String command = consoleInput.readWhileNotBlank("n / д - далее \np / н - назад \nq / в - выход").toLowerCase();
            switch (command) {
                case "n", "д" -> {
                    if (responses.hasNext()) {
                        page++;
                    }
                }
                case "p", "н" -> {
                    if (page > 0) {
                        page--;
                    }
                }
                case "q" -> {
                    return;
                }
                default -> ConsoleOutput.error("Введите n / д, p / н, q / в");
            }
        }
    }

}
