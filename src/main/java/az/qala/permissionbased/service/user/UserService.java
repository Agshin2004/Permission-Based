package az.qala.permissionbased.service.user;

import az.qala.permissionbased.config.CustomUserDetails;
import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.request.user.*;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.user.LoginResponse;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public interface UserService {
    GenericResponse<UserDTO> createUser(@NotNull RegisterRequest request);
    GenericResponse<LoginResponse> loginUser(@NotNull LoginRequest request);
    GenericResponse<String> approveUser(@NotNull ApproveUserRequest approveUserRequest);
    GenericResponse<Map<String, String>> saveProfilePicture(@NotNull UploadProfilePictureRequest uploadProfilePictureRequest, CustomUserDetails userDetails);
    GenericResponse<Map<String, Boolean>> addUserProfileDetails(@NotNull UserProfileUpdateRequest userProfileUpdateRequest, CustomUserDetails userDetails);
}
