package by.may.presentation.command.users;

import by.may.auth.SessionContext;
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
public class ChangeRoleCommand implements Command {

    private final UserService userService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Смена роли";
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
        ConsoleOutput.info("Текущая роль пользователя: " + current.role());

        Role role = consoleInput.readRole("Роль пользователя: ");

        if (role == null) {
            role = current.role();
        }

        userService.changeRole(id, role);
    }
}
