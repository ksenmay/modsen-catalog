package by.may.infrastructure.listener;

import by.may.application.event.ReviewDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewDeletedHandler {
    @EventListener
    public void handle(ReviewDeletedEvent event) {
        System.out.print(event.username() + ", Ваш отзыв удален");
    }
}
