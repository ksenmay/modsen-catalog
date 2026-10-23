package by.may.auth;

import by.may.entity.Role;
import by.may.entity.User;
import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SessionContext {

    private User currentUser;

    public void startSession(User user) {
        this.currentUser = user;
    }

    public void endSession() {
        this.currentUser = null;
    }

    public User requireRole(Role role) {
        if (!isLogged()) {
            throw new IllegalStateException("Session is not active");
        }
        if (currentUser.getRole() != role) {
            throw new AccessDeniedException("Current user " + currentUser.getUsername() + " don't have necessary permission");
        }
        return currentUser;
    }

    public Role getCurrentUserRole() {
        if (!isLogged()) {
            throw new IllegalStateException("Session is not active");
        }
        return currentUser.getRole();
    }

    private boolean isLogged() {
        return currentUser != null;
    }

}
