package by.may.application.service;

import by.may.infrastructure.entity.UserJpaEntity;
import by.may.infrastructure.event.EventPublisher;
import by.may.infrastructure.event.UserLoggedInEvent;
import by.may.domain.exception.UserNotFoundException;
import by.may.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher publisher;

    public UserJpaEntity authenticate(String username, String password) {
        UserJpaEntity userJpaEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );

        if (!passwordEncoder.matches(password, userJpaEntity.getPasswordHash())) {
            throw new UserNotFoundException("Неверный логин или пароль");
        }

        publisher.publish(new UserLoggedInEvent(userJpaEntity.getUsername()));

        return userJpaEntity;
    }

}
