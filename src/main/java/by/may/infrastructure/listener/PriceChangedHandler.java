package by.may.infrastructure.listener;

import by.may.domain.event.PriceChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PriceChangedHandler {
    @EventListener
    public void handle (PriceChangedEvent event) {
        System.out.print("Цена продукта " + event.productName() +
                " изменилась с " + event.oldPrice() +
                " на " + event.newPrice());
    }
}
