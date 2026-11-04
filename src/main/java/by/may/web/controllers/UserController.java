package by.may.web.controllers;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserRegisterRequest;
import by.may.application.dto.UserResponse;
import by.may.application.dto.UserUpdateRequest;
import by.may.application.service.UserService;
import by.may.application.service.UserSortField;
import by.may.domain.Role;
import by.may.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Пользователи", description = "Регистрация и управление пользователями")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Список пользователей",
            description = "Доступно ADMIN, сортировка по username или createdAt")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> getAll(@PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(defaultValue = "USERNAME") UserSortField userSortField,
                                     @RequestParam(defaultValue = "true") boolean asc) {
        return userService.findAll(userSortField, asc, pageable);
    }

    @Operation(summary = "Пользователь по id",
            description = "Доступно ADMIN")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Создать пользователя с любой ролью",
            description = "Доступно ADMIN, создаёт ADMIN, MANAGER или CUSTOMER.")
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@Valid @RequestBody UserCreateRequest dto) {
        return userService.createUser(dto);
    }

    @Operation(summary = "Регистрация",
            description = "Создаёт пользователя с ролью CUSTOMER")
    @PostMapping
    @SecurityRequirements
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRegisterRequest dto) {
        return userService.registerUser(dto);
    }

    @Operation(summary = "Текущий пользователь",
            description = "Возвращает данные пользователя из токена.")
    @GetMapping("/me")
    public UserResponse getCurrent(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.getUserById(currentUser.id());
    }

    @Operation(summary = "Изменить данные пользователя",
            description = "Доступно ADMIN, меняет логин и email")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update (@PathVariable Long id, @Valid @RequestBody UserUpdateRequest dto) {
        return userService.updateUser(id, dto);
    }

    @Operation(summary = "Сменить роль",
            description = "Доступно ADMIN, нельзя понизить последнего администратора")
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse changeRole (@PathVariable Long id, @RequestParam Role role) {
        return userService.changeRole(id, role);
    }

    @Operation(summary = "Удалить пользователя",
            description = "Доступно ADMIN, нельзя удалить себя и последнего администраторан")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id, Long currentUserId) {
        userService.deleteUser(id, currentUserId);
    }

}
