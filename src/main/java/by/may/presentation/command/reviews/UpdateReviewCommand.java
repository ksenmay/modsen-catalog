package by.may.presentation.command.reviews;

import by.may.auth.SessionContext;
import by.may.dto.ReviewCreateRequest;
import by.may.entity.Role;
import by.may.entity.User;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.presentation.console.ConsoleOutput;
import by.may.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateReviewCommand implements Command {

    private final ConsoleInput consoleInput;
    private final ReviewService reviewService;
    private final SessionContext sessionContext;


    @Override
    public String title() {
        return "Редактировать отзыв";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());
        User user = sessionContext.getCurrentUser();

        Long reviewId = consoleInput.readLong("ID отзыва: ");
        int rating = consoleInput.readInt("Новая оценка (1-5): ", 1, 5);
        String comment = consoleInput.readWhileNotBlank("Новый комментарий: ");

        ReviewCreateRequest dto = new ReviewCreateRequest(rating, comment);
        reviewService.updateReview(reviewId, user.getId(), dto);
        ConsoleOutput.success("Отзыв обновлён");
    }
}
