package az.qala.permissionbased.model.dto;

import az.qala.permissionbased.model.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDTO {
    public Long id;
    public UserRoles roleName;
}
