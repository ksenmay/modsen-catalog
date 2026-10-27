package by.may.auth;

import by.may.entity.User;
import by.may.event.UserLoggedInEvent;
import by.may.exception.UserNotFoundException;
import by.may.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Optional<User> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return Optional.empty();
        }

        applicationEventPublisher.publishEvent(new UserLoggedInEvent(user.getUsername()));

        return Optional.of(user);
    }

}
