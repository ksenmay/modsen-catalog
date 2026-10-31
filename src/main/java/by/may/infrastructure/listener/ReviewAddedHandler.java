package by.may.infrastructure.listener;

import by.may.infrastructure.event.ReviewAddedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewAddedHandler {
    @EventListener
    public void handle(ReviewAddedEvent event) {
        System.out.print(event.username() + ", спасибо за отзыв!");
    }
}
