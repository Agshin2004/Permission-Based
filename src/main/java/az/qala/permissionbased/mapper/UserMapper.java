package az.qala.permissionbased.mapper;

import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.User;
import ch.qos.logback.core.model.ComponentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Important: Once MapStruct knows how to map one Role → RoleDTO, it can automatically map a list of them (List<Role> → List<RoleDTO>)
// that's what we need uses = {RoleMapper.class}
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    //@Mapping(source = "id", target = "id") // UUID -> Integer mapping requires a custom method
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "createdAt", target = "created")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "registrationStatus", target = "registrationStatus")
    @Mapping(source = "roles", target = "roles") // Requires Role -> RoleDTO mapping
    UserDTO userToUserDto(User user);

    @Mapping(source = "created", target = "createdAt")
    User userDtoToUser(UserDTO userDTO);
}
