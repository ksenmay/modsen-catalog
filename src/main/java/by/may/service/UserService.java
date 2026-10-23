package by.may.service;

import by.may.dto.UserCreateRequest;
import by.may.dto.UserResponse;
import by.may.dto.UserUpdateRequest;
import by.may.entity.Role;
import by.may.entity.User;
import by.may.event.UserRoleChangedEvent;
import by.may.exception.AdminMinLimitException;
import by.may.exception.UserAlreadyExistsException;
import by.may.exception.UserCannotDeleteYourselfException;
import by.may.exception.UserNotFoundException;
import by.may.mapper.UserMapper;
import by.may.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UserResponse createUser(UserCreateRequest dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("username", dto.username());
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("email", dto.email());
        }

        String hash = passwordEncoder.encode(dto.password());
        User user = new User(dto.username(), dto.email(), hash, dto.role());
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (userRepository.existsByUsernameAndIdNot(dto.username(), id)) {
            throw new UserAlreadyExistsException("username", dto.username());
        }
        if (userRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new UserAlreadyExistsException("email", dto.email());
        }

        user.setEmail(dto.email());
        user.setUsername(dto.username());

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponse changeRole(Long id, Role newRole) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (user.getRole() == newRole) {
            return userMapper.toDto(user);
        }

        if (user.getRole() == Role.ADMIN && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new AdminMinLimitException();
        }

        Role oldRole = user.getRole();
        user.setRole(newRole);

        applicationEventPublisher.publishEvent(new UserRoleChangedEvent(user.getUsername(), oldRole, newRole));

        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new UserCannotDeleteYourselfException();
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (user.getRole() == Role.ADMIN && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new AdminMinLimitException();
        }
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(UserSortField sortField, boolean asc, Pageable pageable) {
        Sort.Direction direction;
        if (asc) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortField.getSortField());
        PageRequest request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return userRepository.findAll(request).map(userMapper::toDto);
    }

}
