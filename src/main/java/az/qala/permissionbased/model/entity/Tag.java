package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.model.enums.Colors;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Colors color;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();
}
