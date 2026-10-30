package by.may.security;

import by.may.entity.Role;

public record CurrentUser(
    Long id,
    String username,
    Role role){}
