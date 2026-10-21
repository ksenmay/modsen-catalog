package by.may.listener;

import by.may.event.ProductDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductDeletedHandler {
    @EventListener
    public void handle(ProductDeletedEvent event) {
        System.out.print("Продукт " + event.productName() + " удалён");
    }
}
