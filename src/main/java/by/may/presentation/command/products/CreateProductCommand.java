package by.may.presentation.command.products;

import by.may.auth.SessionContext;
import by.may.dto.ProductCreateRequest;
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
public class CreateProductCommand implements Command {

    private final ProductService productService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Добавление продукта";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        String name = consoleInput.readWhileNotBlank("Название продукта: ");
        String description = consoleInput.readWhileNotBlank("Описание: ");
        BigDecimal price = consoleInput.readPrice("Цена: ");
        Long categoryId = consoleInput.readLong("ID категории: ");

        ProductCreateRequest dto = new ProductCreateRequest(name, description, price, categoryId);
        productService.createProduct(dto);

        ConsoleOutput.success("Продукт " + name + " создан\n");
    }
}
