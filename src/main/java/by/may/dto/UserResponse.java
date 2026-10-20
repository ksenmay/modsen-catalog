package by.may.dto;

import by.may.entity.Role;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        LocalDate createdAt) {}
