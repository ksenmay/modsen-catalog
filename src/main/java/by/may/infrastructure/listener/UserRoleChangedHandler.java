package by.may.infrastructure.listener;

import by.may.infrastructure.event.UserRoleChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRoleChangedHandler {
    @EventListener
    public void handle(UserRoleChangedEvent event) {
        System.out.print("Роль пользователя " + event.username() +
                " изменена с " + event.oldRole() +
                " на " + event.newRole());
    }
}
