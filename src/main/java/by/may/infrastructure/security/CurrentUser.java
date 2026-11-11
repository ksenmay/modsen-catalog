package by.may.infrastructure.security;

import by.may.domain.Role;

public record CurrentUser(
    Long id,
    String username,
    Role role){}
