package by.may.application.port;

import by.may.application.service.UserSortField;
import by.may.domain.Role;
import by.may.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(Long id);
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
    void delete(Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    long countByRole(Role role);
    Page<User> findAll(Pageable pageable);
    Page<User> findAll(UserSortField sortField, boolean asc, Pageable pageable);
    Optional<User> findByRefreshToken(String refreshToken);
}
