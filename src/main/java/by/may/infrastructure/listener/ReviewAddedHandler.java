package by.may.infrastructure.listener;

import by.may.domain.event.ReviewAddedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewAddedHandler {
    @EventListener
    public void handle(ReviewAddedEvent event) {
        System.out.print("Спасибо за отзыв!");
    }
}
