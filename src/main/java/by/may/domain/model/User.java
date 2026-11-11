package by.may.domain.model;

import by.may.domain.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class User {
    private Long id;
    @Setter
    private String username;
    @Setter
    private String email;
    @Setter
    private String passwordHash;
    @Setter
    private Role role;
    private LocalDate createdAt;
    @Setter
    private String refreshToken;
    @Setter
    private LocalDateTime refreshTokenExpiryTime;

    public User(Long id, String username, String email, String passwordHash, Role role, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(Long id, String username, String email, String passwordHash, Role role, LocalDate createdAt, String refreshToken, LocalDateTime refreshTokenExpiryTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryTime = refreshTokenExpiryTime;
    }

    public User(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
