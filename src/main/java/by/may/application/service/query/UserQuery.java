package by.may.application.service.query;

import by.may.application.dto.UserResponse;
import by.may.application.service.UserSortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {
    UserResponse getUserById(Long id);
    Page<UserResponse> findAll(UserSortField sortField, boolean asc, Pageable pageable);
}
