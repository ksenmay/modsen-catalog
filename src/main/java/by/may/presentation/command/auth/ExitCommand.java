package by.may.presentation.command.auth;

import by.may.auth.SessionContext;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExitCommand implements Command {

    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Выход из системы";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.endSession();
        ConsoleOutput.info("Вы вышли из системы\n");
    }
}