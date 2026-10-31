package by.may.infrastructure.adapter;

import by.may.application.port.UserRepositoryPort;
import by.may.application.service.UserSortField;
import by.may.domain.Role;
import by.may.domain.exception.UserNotFoundException;
import by.may.domain.model.User;
import by.may.infrastructure.entity.UserJpaEntity;
import by.may.infrastructure.mapper.UserJpaMapper;
import by.may.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepositoryPort {

    private final UserRepository repository;
    private final UserJpaMapper jpaMapper;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(jpaMapper::toModel);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return jpaMapper.toModel(repository.save(jpaMapper.toNewEntity(user)));
    }

    @Override
    public void delete(Long id) {
        UserJpaEntity entity = repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        repository.delete(entity);
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return repository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public long countByRole(Role role) {
        return repository.countByRole(role);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(jpaMapper::toModel);
    }

    @Override
    public Page<User> findAll(UserSortField sortField, boolean asc, Pageable pageable) {
        Sort.Direction direction;
        if (asc) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortField.getSortField());
        PageRequest request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAll(request).map(jpaMapper::toModel);
    }
}
