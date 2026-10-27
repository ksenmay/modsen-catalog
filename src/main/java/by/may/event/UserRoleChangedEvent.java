package by.may.event;

import by.may.entity.Role;

public record UserRoleChangedEvent(
        String username,
        Role oldRole,
        Role newRole) {}
