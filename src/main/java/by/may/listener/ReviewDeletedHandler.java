package by.may.listener;

import by.may.event.ReviewDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewDeletedHandler {
    @EventListener
    public void handle(ReviewDeletedEvent event) {
        System.out.print(event.username() + ", Ваш отзыв удален");
    }
}
