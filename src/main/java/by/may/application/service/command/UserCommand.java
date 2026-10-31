package by.may.application.service.command;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserResponse;
import by.may.application.dto.UserUpdateRequest;
import by.may.domain.Role;

public interface UserCommand {
    UserResponse createUser(UserCreateRequest dto);
    UserResponse updateUser(Long id, UserUpdateRequest dto);
    UserResponse changeRole(Long id, Role newRole);
    void deleteUser(Long id, Long currentUserId);
}
