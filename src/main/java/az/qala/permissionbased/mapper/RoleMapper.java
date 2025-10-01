package az.qala.permissionbased.mapper;

import az.qala.permissionbased.model.dto.RoleDTO;
import az.qala.permissionbased.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDTO roleDTO);
}
