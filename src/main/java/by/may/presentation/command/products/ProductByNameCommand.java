package by.may.presentation.command.products;

import by.may.auth.SessionContext;
import by.may.dto.ProductResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ProductSearchCriteria;
import by.may.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductByNameCommand implements Command {

    private final ProductService productService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Поиск продукта по названию";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        String name = consoleInput.readWhileNotBlank("Название продукта: ");

        ProductSearchCriteria criteria = new ProductSearchCriteria(name, null, null, null, null, null, true);

        Page<ProductResponse> page = productService.searchProducts(criteria, PageRequest.of(0, 10));

        if (page.isEmpty()) {
            ConsoleOutput.info("Продукты не найдены\n");
            return;
        }

        page.getContent().forEach(ConsoleOutput::printProduct);
    }
}