package az.qala.permissionbased.model.entity.composite;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

// all composite keys must implement Serializable
// a composite key is a primary key made of more than one column
// @Embeddable - tells JPA this class can be embedded in another entity as key
// like a tuple key: POST_TAG = (postId, tagId)
@Embeddable
@Data
public class PostTagId implements Serializable {
    private Long postId;
    private Long tagId;
}
