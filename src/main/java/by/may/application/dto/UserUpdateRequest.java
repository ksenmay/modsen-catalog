package by.may.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
        @NotBlank @Size(min = 3, max = 50)
        String username,
        @NotBlank @Email @Size(max = 255)
        String email){}
