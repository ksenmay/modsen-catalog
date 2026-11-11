package by.may.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Category {
    private Long id;
    @Setter
    private String name;
    @Setter
    private String description;

    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
