package by.may.presentation.command.reviews;

import by.may.auth.SessionContext;
import by.may.dto.ReviewCreateRequest;
import by.may.entity.Role;
import by.may.entity.User;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddReviewCommand implements Command {

    private final ConsoleInput consoleInput;
    private final ReviewService reviewService;
    private final SessionContext sessionContext;


    @Override
    public String title() {
        return "Добавить отзыв";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());
        User user = sessionContext.getCurrentUser();

        Long productId = consoleInput.readLong("ID продукта: ");
        int rating = consoleInput.readInt("Оценка (1-5): ", 1, 5);
        String comment = consoleInput.readWhileNotBlank("Комментарий: ");

        ReviewCreateRequest dto = new ReviewCreateRequest(rating, comment);
        reviewService.createReview(productId, user.getId(), dto);
    }
}
