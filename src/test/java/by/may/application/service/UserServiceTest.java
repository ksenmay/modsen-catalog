package by.may.application.service;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserRegisterRequest;
import by.may.application.port.EventPublisher;
import by.may.application.port.PasswordHasher;
import by.may.application.port.UserRepositoryPort;
import by.may.domain.Role;
import by.may.domain.event.UserRoleChangedEvent;
import by.may.domain.exception.AdminMinLimitException;
import by.may.domain.exception.UserAlreadyExistsException;
import by.may.domain.exception.UserCannotDeleteYourselfException;
import by.may.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordHasher passwordHasher;
    @Mock private EventPublisher publisher;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserCreatesCustomerWithHashedPassword() {
        UserRegisterRequest dto = new UserRegisterRequest("ivan", "ivan@mail.ru", "password1");
        when(userRepositoryPort.existsByUsername("ivan")).thenReturn(false);
        when(userRepositoryPort.existsByEmail("ivan@mail.ru")).thenReturn(false);
        when(passwordHasher.hash("password1")).thenReturn("hash");

        userService.registerUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(captor.capture());
        assertEquals(Role.CUSTOMER, captor.getValue().getRole());
        assertEquals("hash", captor.getValue().getPasswordHash());
    }

    @Test
    void createUserThrowsWhenUsernameTaken() {
        UserCreateRequest dto = new UserCreateRequest("ivan", "ivan@mail.ru", "password1", Role.MANAGER);
        when(userRepositoryPort.existsByUsername("ivan")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void changeRoleOfLastAdminIsForbidden() {
        User admin = new User(1L, "root", "root@mail.ru", "hash", Role.ADMIN, LocalDate.now());
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepositoryPort.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThrows(AdminMinLimitException.class, () -> userService.changeRole(1L, Role.CUSTOMER));
    }

    @Test
    void changeRolePublishesEventWithOldAndNewRole() {
        User user = new User(2L, "ivan", "ivan@mail.ru", "hash", Role.CUSTOMER, LocalDate.now());
        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(user));

        userService.changeRole(2L, Role.MANAGER);

        verify(publisher).publish(argThat(e -> e instanceof UserRoleChangedEvent));
    }

    @Test
    void deleteUserForbidsDeletingYourself() {
        assertThrows(UserCannotDeleteYourselfException.class,
                () -> userService.deleteUser(1L, 1L));
        verify(userRepositoryPort, never()).delete(any());
    }

    @Test
    void deleteUserOfLastAdminIsForbidden() {
        User admin = new User(3L, "root", "root@mail.ru", "hash", Role.ADMIN, LocalDate.now());
        when(userRepositoryPort.findById(3L)).thenReturn(Optional.of(admin));
        when(userRepositoryPort.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThrows(AdminMinLimitException.class, () -> userService.deleteUser(3L, 99L));
    }
}
