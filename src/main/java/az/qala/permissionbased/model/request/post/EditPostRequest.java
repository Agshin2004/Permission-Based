package az.qala.permissionbased.model.request.post;

import az.qala.permissionbased.model.entity.PostTag;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class EditPostRequest implements Serializable {
    private String title;
    private String description;
    private Set<PostTag> tagIds;
}
