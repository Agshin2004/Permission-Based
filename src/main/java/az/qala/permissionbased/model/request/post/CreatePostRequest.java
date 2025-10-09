package az.qala.permissionbased.model.request.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class CreatePostRequest implements Serializable {
    @NotNull(message = "Title must be provided")
    private String title;

    private String description;

    private Set<Long> tagIds;
}
