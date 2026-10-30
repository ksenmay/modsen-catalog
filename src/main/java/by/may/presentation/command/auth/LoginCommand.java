package by.may.presentation.command.auth;

import by.may.auth.AuthService;
import by.may.auth.SessionContext;
import by.may.entity.Role;
import by.may.entity.User;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LoginCommand implements Command {

    private final AuthService authService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Вход";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of();
    }

    @Override
    public void execute() {
        if(sessionContext.isLogged()) {
            ConsoleOutput.info("Вы уже вошли в систему");
            return;
        }

        String username = consoleInput.readWhileNotBlank("Логин: ");
        String password = consoleInput.readWhileNotBlank("Пароль: ");

        User user = authService.authenticate(username, password);

        sessionContext.startSession(user);
        ConsoleOutput.success("Вход выполнен\n");
    }
}
