package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    public static final String ID_FIELD = "id";
    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL_FIELD = "email";
    public static final String DELETED_FIELD = "deleted";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private Integer id;

    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String username;

    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String email;

    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updated = LocalDateTime.now();

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false)
    private RegistrationStatus registrationStatus;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    // Helper method to add a role
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }

        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    public boolean hasRole(Role role) {
        return this.roles != null && this.roles.contains(role);
    }
}
