package by.may.listener;

import by.may.event.PriceChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PriceChangeHandler {
    @EventListener
    public void handle (PriceChangeEvent event) {
        System.out.print("Цена продукта " + event.productName() +
                " изменилась с " + event.oldPrice() +
                " на " + event.newPrice());
    }
}
