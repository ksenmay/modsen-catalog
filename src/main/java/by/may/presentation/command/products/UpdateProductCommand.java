package by.may.presentation.command.products;

import by.may.auth.SessionContext;
import by.may.dto.ProductCreateRequest;
import by.may.dto.ProductResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateProductCommand implements Command {

    private final ProductService productService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Редактирование продукта";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        Long id = consoleInput.readLong("ID продукта: ");
        ProductResponse response = productService.findById(id);

        ConsoleOutput.printProduct(response);

        String name = consoleInput.readWhileNotBlank("Название продукта: ");
        String description = consoleInput.readWhileNotBlank("Описание: ");
        BigDecimal price = consoleInput.readPrice("Цена: ");
        Long categoryId = consoleInput.readLong("ID категории: ");

        if (name == null) {
            name = response.name();
        }
        if (description == null) {
            description = response.description();
        }
        if (price == null) {
            price = response.price();
        }
        if (categoryId == null) {
            categoryId = response.categoryId();
        }

        ProductCreateRequest dto = new ProductCreateRequest(name, description, price, categoryId);
        productService.updateProduct(id, dto);

        ConsoleOutput.success("Продукт " + name + " отредактирован\n");
    }
}
