package az.qala.permissionbased.model.request.tag;

import az.qala.permissionbased.model.enums.Colors;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class TagRequest implements Serializable {
    @NotNull(message = "tag name cannot be null")
    private String name;
    private String description;
    private Colors color;
}
