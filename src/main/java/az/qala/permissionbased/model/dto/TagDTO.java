package az.qala.permissionbased.model.dto;

import az.qala.permissionbased.model.enums.Colors;
import lombok.Builder;

@Builder
public record TagDTO(String name, String description, Colors color) {
}
