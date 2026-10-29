package by.may.presentation.console;

import by.may.presentation.command.Command;
import by.may.presentation.command.auth.ExitCommand;
import by.may.presentation.command.invoker.CommandInvoker;
import by.may.presentation.menu.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleApp implements CommandLineRunner {

    private final Menu menu;
    private final CommandInvoker invoker;

    @Override
    public void run(String... args) {
        while (true) {
            List<Command> commands = menu.availableCommands();
            log.error(String.valueOf(commands.size()));
            if (commands.isEmpty()) {
                ConsoleOutput.error("Внутренняя ошибка");
                return;
            }
            menu.print(commands);
            Command command = menu.select(commands);
            if (command instanceof ExitCommand) {
                return;
            }
            invoker.invoke(command);
        }
    }
}
