package by.may.presentation.command.categories;

import by.may.auth.SessionContext;
import by.may.dto.CategoryCreateRequest;
import by.may.dto.CategoryResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateCategoryCommand implements Command {

    private final SessionContext sessionContext;
    private final CategoryService categoryService;
    private final ConsoleInput consoleInput;

    @Override
    public String title() {
        return "Редактирование категории";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        String name = consoleInput.readWhileNotBlank("Название категории: ");
        CategoryResponse response = categoryService.getCategoryByName(name);

        ConsoleOutput.info("Текущее описание категории: " + response.description());

        String newName = consoleInput.readWhileNotBlank("Название категории: ");
        String description = consoleInput.readWhileNotBlank("Описание: ");

        if (newName == null) {
            newName = response.name();
        }
        if (description == null) {
            description = response.description();
        }

        CategoryCreateRequest dto = new CategoryCreateRequest(newName, description);
        categoryService.createCategory(dto);

        ConsoleOutput.success("Категория " + newName + " отредактирована");

    }
}
