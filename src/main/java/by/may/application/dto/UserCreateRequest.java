package by.may.application.dto;

import by.may.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest (
        @NotBlank @Size(min = 3, max = 50)
        String username,
        @NotBlank @Email @Size(max = 255)
        String email,
        @NotBlank @Size(min = 8, max = 100)
        String password,
        @NotNull
        Role role) {}
