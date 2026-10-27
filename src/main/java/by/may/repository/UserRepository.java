package by.may.repository;

import by.may.entity.Role;
import by.may.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    long countByRole(Role role);

    Optional<User> findByUsername(String username);
}
