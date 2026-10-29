package by.may.controllers;

import by.may.dto.UserCreateRequest;
import by.may.dto.UserResponse;
import by.may.dto.UserUpdateRequest;
import by.may.entity.Role;
import by.may.service.UserService;
import by.may.service.UserSortField;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getAll(@PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(defaultValue = "USERNAME") UserSortField userSortField,
                                     @RequestParam(defaultValue = "true") boolean asc) {
        return userService.findAll(userSortField, asc, pageable);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserResponse create (@Valid @RequestBody UserCreateRequest dto) {
        return userService.createUser(dto);
    }

    @PutMapping
    public UserResponse update (@PathVariable Long id, @Valid @RequestBody UserUpdateRequest dto) {
        return userService.updateUser(id, dto);
    }

    @PatchMapping
    public UserResponse changeRole (@PathVariable Long id, @RequestParam Role role) {
        return userService.changeRole(id, role);
    }

    @DeleteMapping
    public void delete (@PathVariable Long id, Long currentUserId) {
        userService.deleteUser(id, currentUserId);
    }

}
