package by.may.web.controllers;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserRegisterRequest;
import by.may.application.dto.UserResponse;
import by.may.application.dto.UserUpdateRequest;
import by.may.application.service.UserService;
import by.may.application.service.UserSortField;
import by.may.domain.Role;
import by.may.infrastructure.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> getAll(@PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(defaultValue = "USERNAME") UserSortField userSortField,
                                     @RequestParam(defaultValue = "true") boolean asc) {
        return userService.findAll(userSortField, asc, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@Valid @RequestBody UserCreateRequest dto) {
        return userService.createUser(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRegisterRequest dto) {
        return userService.registerUser(dto);
    }

    @GetMapping("/me")
    public UserResponse getCurrent(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.getUserById(currentUser.id());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update (@PathVariable Long id, @Valid @RequestBody UserUpdateRequest dto) {
        return userService.updateUser(id, dto);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse changeRole (@PathVariable Long id, @RequestParam Role role) {
        return userService.changeRole(id, role);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id, Long currentUserId) {
        userService.deleteUser(id, currentUserId);
    }

}
