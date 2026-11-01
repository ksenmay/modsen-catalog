package by.may.infrastructure.listener;

import by.may.domain.event.ReviewDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewDeletedHandler {
    @EventListener
    public void handle(ReviewDeletedEvent event) {
        System.out.print("Отзыв удален");
    }
}
