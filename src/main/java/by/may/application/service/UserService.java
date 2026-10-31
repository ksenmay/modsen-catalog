package by.may.application.service;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserResponse;
import by.may.application.dto.UserUpdateRequest;
import by.may.application.port.PasswordHasher;
import by.may.application.port.UserRepositoryPort;
import by.may.application.service.command.UserCommand;
import by.may.application.service.query.UserQuery;
import by.may.domain.Role;
import by.may.domain.model.User;
import by.may.infrastructure.entity.UserJpaEntity;
import by.may.domain.exception.AdminMinLimitException;
import by.may.domain.exception.UserAlreadyExistsException;
import by.may.domain.exception.UserCannotDeleteYourselfException;
import by.may.domain.exception.UserNotFoundException;
import by.may.application.mapper.UserMapper;
import by.may.infrastructure.event.EventPublisher;
import by.may.infrastructure.event.UserRoleChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserCommand, UserQuery {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;
    private final PasswordHasher passwordHasher;
    private final EventPublisher publisher;

    @Transactional
    public UserResponse createUser(UserCreateRequest dto) {
        if (userRepositoryPort.existsByUsername(dto.username())) {
            log.error("пользователь с {} уже существует", dto.username());
            throw new UserAlreadyExistsException("username", dto.username());
        }

        if (userRepositoryPort.existsByEmail(dto.email())) {
            log.error("пользователь с {} уже существует", dto.email());
            throw new UserAlreadyExistsException("email", dto.email());
        }

        String hash = passwordHasher.hash(dto.password());
        User user = new User(dto.username(), dto.email(), hash, dto.role());
        log.info("создан пользователь {} с ролью {}", dto.username(), dto.role());
        return userMapper.toDto(userRepositoryPort.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest dto) {
        User user = userRepositoryPort.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        if (userRepositoryPort.existsByUsernameAndIdNot(dto.username(), id)) {
            log.error("пользователь с {}, {} уже существует", user.getId(), dto.username());
            throw new UserAlreadyExistsException("username", dto.username());
        }
        if (userRepositoryPort.existsByEmailAndIdNot(dto.email(), id)) {
            log.error("пользователь с {}, {} уже существует", user.getId(), dto.email());
            throw new UserAlreadyExistsException("email", dto.email());
        }

        user.setEmail(dto.email());
        user.setUsername(dto.username());
        log.info("изменён пользователь {}, {}", user.getId(), user.getUsername());
        return userMapper.toDto(userRepositoryPort.save(user));
    }

    @Transactional
        public UserResponse changeRole(Long id, Role newRole) {
        User user = userRepositoryPort.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );

        Role oldRole = user.getRole();

        if (oldRole == newRole) {
            return userMapper.toDto(user);
        }

        if (oldRole == Role.ADMIN && userRepositoryPort.countByRole(Role.ADMIN) <= 1) {
            log.error("нельзя убрать последнего администратора {} путём смены роли", user.getId());
            throw new AdminMinLimitException();
        }

        user.setRole(newRole);
        publisher.publish(new UserRoleChangedEvent(user.getUsername(), oldRole, newRole));
        log.info("роль пользователя {} {} изменеа с {} на {}", user.getUsername(), user.getId(), oldRole, newRole);
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new UserCannotDeleteYourselfException();
        }

        User user  = userRepositoryPort.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        if (user.getRole() == Role.ADMIN && userRepositoryPort.countByRole(Role.ADMIN) <= 1) {
            log.error("нельзя удалить последнего администратора {}", user.getId());
            throw new AdminMinLimitException();
        }
        userRepositoryPort.delete(id);
        log.info("пользователь {} удалён пользователем {}", user.getId(), currentUserId);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepositoryPort.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(UserSortField sortField, boolean asc, Pageable pageable) {
        return userRepositoryPort.findAll(sortField, asc, pageable).map(userMapper::toDto);
    }
}
