package by.may.presentation.command.reviews;

import by.may.auth.SessionContext;
import by.may.dto.ReviewResponse;
import by.may.dto.UserResponse;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ListReviewsCommand implements Command {

    private final ConsoleInput consoleInput;
    private final ReviewService reviewService;
    private final SessionContext sessionContext;


    @Override
    public String title() {
        return "Просмотр отзывов";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        Long productId = consoleInput.readLong("ID продукта: ");

        int page = 0;
        while (true) {
            Page<ReviewResponse> responses = reviewService.getReviewsByProduct(productId, PageRequest.of(page, 10));
            ConsoleOutput.printReviewPage(responses);

            String command = consoleInput.readWhileNotBlank("n / д - далее \np / н - назад \nq / в - выход\n").toLowerCase();
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
