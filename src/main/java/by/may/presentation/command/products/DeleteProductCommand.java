package by.may.presentation.command.products;

import by.may.auth.SessionContext;
import by.may.dto.CategoryResponse;
import by.may.dto.ProductResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteProductCommand implements Command {

    private final ProductService productService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Удаление продукта";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        Long id = consoleInput.readLong("ID продукта: ");
        ProductResponse response = productService.findById(id);
        productService.deleteProduct(response.id());
    }
}
