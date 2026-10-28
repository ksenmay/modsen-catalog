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
public class DeleteCategoryCommand implements Command {

    private final SessionContext sessionContext;
    private final CategoryService categoryService;
    private final ConsoleInput consoleInput;

    @Override
    public String title() {
        return "Удаление категории";
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

        categoryService.deleteCategory(response.id());
    }

}
