package by.may.presentation.command.reviews;

import by.may.auth.SessionContext;
import by.may.entity.Role;
import by.may.presentation.command.Command;
import by.may.presentation.console.ConsoleInput;
import by.may.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteReviewCommand implements Command {

    private final ConsoleInput consoleInput;
    private final ReviewService reviewService;
    private final SessionContext sessionContext;


    @Override
    public String title() {
        return "Удаление отзыва";
    }

    @Override
    public Set<Role> permissibleRoles() {
        return Set.of(Role.ADMIN, Role.MANAGER, Role.CUSTOMER);
    }

    @Override
    public void execute() {
        sessionContext.requireAnyRole(permissibleRoles());

        Long reviewId = consoleInput.readLong("ID отзыва: ");
        if (!consoleInput.isConfirm("Удалить отзыв?\n")) {
            return;
        }

        reviewService.deleteReview(reviewId, sessionContext.getCurrentUser().getId());
    }
}
