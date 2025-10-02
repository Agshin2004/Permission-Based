package az.qala.permissionbased.model.entity;


import az.qala.permissionbased.model.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    // When saving → write roleName.roleName() to DB ("ADMIN", "USER").
    // When reading → take the string from DB ("ADMIN") and convert it back into UserRoles.ADMIN.
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private UserRoles roleName = UserRoles.USER;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.MERGE)
    @JsonIgnore
    // cascade = MERGE -> when Role is updated (merged), changes to its Users are also merged
    private List<User> users = new ArrayList<>();
}
