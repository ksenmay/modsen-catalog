package by.may.infrastructure.repository;

import by.may.domain.Role;
import by.may.infrastructure.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRole(Role role);
    Optional<UserJpaEntity> findByUsername(String username);
    Optional<UserJpaEntity> findByRefreshToken(String refreshToken);
}
