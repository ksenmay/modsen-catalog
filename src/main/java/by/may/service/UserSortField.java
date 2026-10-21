package by.may.service;

import lombok.Getter;

@Getter
public enum UserSortField {
    USERNAME ("username"),
    CREATED_AT ("created_at");

    private final String sortField;

    UserSortField(String sortField) {
        this.sortField = sortField;
    }

}
