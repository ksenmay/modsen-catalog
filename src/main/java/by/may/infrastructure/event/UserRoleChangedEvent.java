package by.may.infrastructure.event;

import by.may.domain.Role;

public record UserRoleChangedEvent(
        String username,
        Role oldRole,
        Role newRole) {}
