package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.model.entity.composite.PostTagId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_tags")
@Getter
@Setter
public class PostTag {
    // @EmbddedId - embeds PostTagId as primary key of this entity so it will have 2
    @EmbeddedId
    private PostTagId id = new PostTagId();

    // Each PostTag can have 1 post and 1 tag but Post and Tag can have many PostTags
    // This points back to the parent Post
    @ManyToOne
    @MapsId("postId") // coming from PostTagId composite key
    @JsonIgnore
    private Post post;

    @ManyToOne
    @MapsId("tagId")
    @JsonIgnore
    private Tag tag;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();
}
