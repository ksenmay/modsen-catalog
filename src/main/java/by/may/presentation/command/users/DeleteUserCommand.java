package by.may.presentation.command.users;

import by.may.auth.SessionContext;
import by.may.entity.Role;
import by.may.entity.User;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteUserCommand implements Command {

    private final UserService userService;
    private final  SessionContext sessionContext;
    private final ConsoleInput consoleInput;

    @Override
    public String title() {
        return "Удаление пользователя";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN);
    }

    @Override
    public void execute() {
        User currentUser = sessionContext.getCurrentUser();
        Long id = consoleInput.readLong("ID пользователя: ");

        if (currentUser.getId().equals(id)) {
            ConsoleOutput.error("Нельзя удалить самого себя");
            return;
        }

        userService.deleteUser(id, currentUser.getId());
        ConsoleOutput.success("Пользователь с ID = " + id + " успешно удалён");
    }
}
