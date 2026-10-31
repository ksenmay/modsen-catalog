package by.may.infrastructure.listener;

import by.may.infrastructure.event.UserLoggedInEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedInHandler {
    @EventListener
    public void handle(UserLoggedInEvent event) {
        System.out.print("Добро пожаловать, " + event.username());
    }
}
