package by.may.presentation.command.invoker;

import by.may.auth.SessionContext;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandInvoker {

    public final SessionContext sessionContext;

    public void invoke(Command command) {
        try {
            command.execute();
        } catch (Exception e) {
            ConsoleOutput.error("Внутренняя ошибка\n");
        }
    }

}
