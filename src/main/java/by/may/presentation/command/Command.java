package by.may.presentation.command;

import by.may.entity.Role;

import java.util.Set;

public interface Command {
    String title();
    Set<Role> permissibleRoles();
    void execute();
}
