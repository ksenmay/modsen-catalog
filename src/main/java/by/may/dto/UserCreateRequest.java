package by.may.dto;

import by.may.entity.Role;

public record UserCreateRequest (
    String username,
    String email,
    String password,
    Role role) {}
