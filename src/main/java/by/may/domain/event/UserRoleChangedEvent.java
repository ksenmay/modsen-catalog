package by.may.domain.event;

import by.may.domain.Role;

public record UserRoleChangedEvent(
        String username,
        Role oldRole,
        Role newRole) {}
