package by.may.service;

import lombok.Getter;

@Getter
public enum ProductSortField {
    PRICE("price"),
    CREATED_AT("createdAt"),
    AVERAGE_RATING("averageRating");

    private final String field;

    ProductSortField(String field) {
        this.field = field;
    }
}
