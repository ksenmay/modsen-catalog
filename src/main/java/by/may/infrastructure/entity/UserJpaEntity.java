package by.may.infrastructure.entity;

import by.may.domain.Role;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import lombok.AccessLevel;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.SQLRestriction;

    import java.time.LocalDate;
    import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Setter
    @Column (nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name="refresh_token", length=255)
    @Setter
    private String refreshToken;

    @Column(name = "refresh_token_expiry_time")
    @Setter
    private LocalDateTime refreshTokenExpiryTime;

    public UserJpaEntity(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDate.now();
    }
    }
