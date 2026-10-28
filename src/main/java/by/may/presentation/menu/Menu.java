package by.may.presentation.menu;

import by.may.auth.SessionContext;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Menu {

    private final List<Command> commands;
    private final SessionContext sessionContext;
    private final ConsoleInput consoleInput;

    public List<Command> availableCommands() {
        if (!sessionContext.isLogged()) {
            return commands.stream()
                    .filter(c -> c.permissibleRoles().isEmpty())
                    .toList();
        }
        Role role = sessionContext.getCurrentUserRole();
        return commands.stream()
                .filter(c -> c.permissibleRoles().contains(role))
                .toList();
    }

    public void print(List<Command> available) {
        ConsoleOutput.title("Меню");
        for (int i = 0; i < available.size(); i++) {
            ConsoleOutput.info((i+1) + ". " + available.get(i).title());
        }
    }

    public Command select(List<Command> available) {
        int choice = consoleInput.readInt("Выберете команду по номеру: ", 1, available.size());
        return available.get(choice - 1);
    }

}
