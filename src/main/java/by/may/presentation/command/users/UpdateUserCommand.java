package by.may.presentation.command.users;

import by.may.auth.SessionContext;
import by.may.dto.UserResponse;
import by.may.dto.UserUpdateRequest;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateUserCommand implements Command {

    private final UserService userService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Редактирование данных пользователя";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN);
    }

    @Override
    public void execute() {
        sessionContext.requireRole(Role.ADMIN);

        Long id = consoleInput.readLong("ID пользователя: ");
        UserResponse current = userService.getUserById(id);
        ConsoleOutput.info("Текущее имя пользователя: " + current.username());
        ConsoleOutput.info("Текущая почта: " + current.email());

        String username = consoleInput.readWhileNotBlank("Логин: ");
        String email = consoleInput.readWhileNotBlank("Электронная почта: ");

        if (username == null) {
            username = current.username();
        }
        if (email == null) {
            email = current.email();
        }

        UserUpdateRequest dto = new UserUpdateRequest(username, email);
        userService.updateUser(id, dto);
        ConsoleOutput.success("Данные пользователя обновлены\n");
    }
}
