package by.may.mapper;

import by.may.dto.UserCreateRequest;
import by.may.dto.UserResponse;
import by.may.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest dto, String passwordHash) {
        return new User(
                dto.username(),
                dto.email(),
                passwordHash,
                dto.role()
        );
    }

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

}
