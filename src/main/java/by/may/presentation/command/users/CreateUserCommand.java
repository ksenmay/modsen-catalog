package by.may.presentation.command.users;

import by.may.auth.SessionContext;
import by.may.dto.UserCreateRequest;
import by.may.dto.UserResponse;
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
public class CreateUserCommand implements Command {

    private final UserService userService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Добавление нового пользователя";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN);
    }

    @Override
    public void execute() {
        sessionContext.requireRole(Role.ADMIN);

        String username = consoleInput.readWhileNotBlank("Логин: ");
        String email = consoleInput.readWhileNotBlank("Электронная почта: ");
        String password = consoleInput.readWhileNotBlank("Пароль: ");
        Role role = consoleInput.readRole("Введите роль (ADMIN, MANAGER, CUSTOMER): ");

        UserCreateRequest dto = new UserCreateRequest(username, email, password, role);
        userService.createUser(dto);

        ConsoleOutput.success("Пользователь " + username + " создан\n");
    }
}
