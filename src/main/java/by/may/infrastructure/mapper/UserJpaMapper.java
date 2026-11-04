package by.may.infrastructure.mapper;

import by.may.domain.model.User;
import by.may.infrastructure.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public User toModel(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getRefreshToken(),
                entity.getRefreshTokenExpiryTime()
        );
    }

    public UserJpaEntity toNewEntity(User model) {
        return new UserJpaEntity(
                model.getUsername(),
                model.getEmail(),
                model.getPasswordHash(),
                model.getRole()
        );
    }

    public UserJpaEntity updateEntity(User model, UserJpaEntity entity) {
        entity.setUsername(model.getUsername());
        entity.setEmail(model.getEmail());
        entity.setRole(model.getRole());
        entity.setPasswordHash(model.getPasswordHash());
        entity.setRefreshToken(model.getRefreshToken());
        entity.setRefreshTokenExpiryTime(model.getRefreshTokenExpiryTime());
        return entity;
    }

}
