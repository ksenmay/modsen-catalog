package by.may.presentation.command.products;

import by.may.auth.SessionContext;
import by.may.dto.ProductResponse;
import by.may.dto.UserResponse;
import by.may.entity.Product;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ProductSearchCriteria;
import by.may.service.ProductService;
import by.may.service.ProductSortField;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchProductCommand implements Command {

    private final ProductService productService;
    private final ConsoleInput consoleInput;
    private final SessionContext sessionContext;

    @Override
    public String title() {
        return "Просмотр всех продуктов (опционально: фильтрация, сортировка)";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        ConsoleOutput.info("Enter - пропуск ввода");

        String name = consoleInput.readOptional("Название: ");
        Long categoryId = consoleInput.readOptionalLong("ID категории: ");
        BigDecimal minPrice = consoleInput.readOptionalPrice("Минимальная цена: ");
        BigDecimal maxPrice = consoleInput.readOptionalPrice("Максимальная цена: ");
        Double rating = consoleInput.readOptionalRating("Рейтинг: ");
        ProductSortField productSortField = consoleInput.readProductSortField("1 - сортировка по цене, 2 - по рейтингу, 3 - по дате создания\n");
        boolean asc = consoleInput.isConfirm("По возрастанию?\n");

        ProductSearchCriteria criteria = new ProductSearchCriteria(name, categoryId, minPrice, maxPrice, rating, productSortField, asc);
        int page = 0;
        while (true) {
            Page<ProductResponse> responses = productService.searchProducts(criteria, PageRequest.of(page, 10));
            ConsoleOutput.printProductPage(responses);

            String command = consoleInput.readWhileNotBlank("n / д - далее \np / н - назад \nq / в - выход").toLowerCase();
            switch (command) {
                case "n", "д" -> {
                    if (responses.hasNext()) {
                        page++;
                    }
                }
                case "p", "н" -> {
                    if (page > 0) {
                        page--;
                    }
                }
                case "q" -> {
                    return;
                }
                default -> ConsoleOutput.error("Введите n / д, p / н, q / в\n");
            }
        }
    }
}
