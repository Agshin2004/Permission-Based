package az.qala.permissionbased.service.user;

import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.request.auth.LoginRequest;
import az.qala.permissionbased.model.request.auth.ApproveUserRequest;
import az.qala.permissionbased.model.request.auth.RegisterRequest;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.auth.LoginResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    GenericResponse<UserDTO> createUser(@NotNull RegisterRequest request);
    GenericResponse<LoginResponse> loginUser(@NotNull LoginRequest request);
    GenericResponse<String> approveUser(@NotNull ApproveUserRequest approveUserRequest);
}
