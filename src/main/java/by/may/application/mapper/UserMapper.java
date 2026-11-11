package by.may.application.mapper;

import by.may.application.dto.UserCreateRequest;
import by.may.application.dto.UserRegisterRequest;
import by.may.application.dto.UserResponse;
import by.may.domain.Role;
import by.may.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(UserCreateRequest dto, String passwordHash) {
        return new User(
                dto.username(),
                dto.email(),
                passwordHash,
                dto.role()
        );
    }

    public User toModel(UserRegisterRequest dto, String passwordHash) {
        return new User(
                dto.username(),
                dto.email(), passwordHash,
                Role.CUSTOMER
        );
    }

    public UserResponse toDto(User model) {
        return new UserResponse(
                model.getId(),
                model.getUsername(),
                model.getEmail(),
                model.getRole(),
                model.getCreatedAt()
        );
    }

}
