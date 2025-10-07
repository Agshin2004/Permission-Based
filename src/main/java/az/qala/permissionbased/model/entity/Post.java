package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.model.entity.mappedClasses.DateTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post extends DateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;


    // one Post can have many PostTag entities
    // mappedBy does not create a separate foreign key column.
    // The foreign key lives in the child table (PostTag.post_id)
    // JPA just uses it to know which PostTag rows belong to this Post.
    // If a PostTag is removed from the postTags collection, JPA will delete it from the database automatically
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags = new HashSet<>();
}
