package by.may.application.dto;

import by.may.domain.Role;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        LocalDate createdAt) {}
