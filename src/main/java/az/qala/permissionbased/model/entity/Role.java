package az.qala.permissionbased.model.entity;


import az.qala.permissionbased.model.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private UserRoles roleName = UserRoles.USER;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.MERGE)
    // cascade = MERGE -> when Role is updated (merged), changes to its Users are also merged
    private List<User> users = new ArrayList<>();
}
