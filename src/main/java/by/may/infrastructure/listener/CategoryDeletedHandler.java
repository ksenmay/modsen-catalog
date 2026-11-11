package by.may.infrastructure.listener;

import by.may.domain.event.CategoryDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryDeletedHandler {
    @EventListener
    public void handle(CategoryDeletedEvent event) {
        System.out.print("Категория " + event.categoryName() + " удалена");
    }
}
