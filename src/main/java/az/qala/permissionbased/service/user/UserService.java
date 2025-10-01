package az.qala.permissionbased.service.user;

import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.request.auth.RegisterResponse;
import az.qala.permissionbased.model.response.GenericResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    GenericResponse<UserDTO> createUser(@NotNull RegisterResponse request);
}
