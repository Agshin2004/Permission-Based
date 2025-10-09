package az.qala.permissionbased.model.dto;

import az.qala.permissionbased.model.entity.PostTag;
import lombok.Builder;

import java.util.Set;

@Builder
public record PostDTO(String title, String description, Set<TagDTO> tags) {
}
