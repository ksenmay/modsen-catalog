package by.may.presentation.command.users;

import by.may.auth.SessionContext;
import by.may.dto.UserResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.UserService;
import by.may.service.UserSortField;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ListUsersCommand implements Command {
    private final UserService userService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Список пользователей";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN);
    }

    @Override
    public void execute() {
        sessionContext.requireRole(Role.ADMIN);

        UserSortField sortField = consoleInput.readUserSortField("1 - сортировка по имени пользователя, " +
                "\n2 - сортировка по дате создания");

        boolean asc = consoleInput.isConfirm("По возрастанию?");

        int page = 0;
        while (true) {
            Page<UserResponse> responses = userService.findAll(sortField, asc, PageRequest.of(page, 10));
            ConsoleOutput.printUserPage(responses);

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
