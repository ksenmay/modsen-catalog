package by.may.listener;

import by.may.event.UserLoggedInEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedInHandler {
    @EventListener
    public void handle(UserLoggedInEvent event) {
        System.out.print("Добро пожаловать, " + event.username());
    }
}
