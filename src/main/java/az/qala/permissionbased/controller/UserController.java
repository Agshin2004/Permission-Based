package az.qala.permissionbased.controller;

import az.qala.permissionbased.config.CustomUserDetails;
import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.request.user.*;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.user.LoginResponse;
import az.qala.permissionbased.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<UserDTO>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        GenericResponse<UserDTO> response = userService.createUser(registerRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        GenericResponse<LoginResponse> response = userService.loginUser(loginRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve-user")
    public ResponseEntity<GenericResponse<String>> approveUser(@Valid @RequestBody ApproveUserRequest approveUserRequest) {
        GenericResponse<String> response = userService.approveUser(approveUserRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<GenericResponse<Map<String, String>>> uploadProfilePicture(
            @ModelAttribute UploadProfilePictureRequest uploadProfilePictureRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GenericResponse<Map<String, String>> response = userService.saveProfilePicture(uploadProfilePictureRequest, userDetails);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/details")
    public ResponseEntity<GenericResponse<Map<String, Boolean>>> addUserProfileDetails(
            @Valid @RequestBody UserProfileUpdateRequest userProfileUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        GenericResponse<Map<String, Boolean>> response = userService.addUserProfileDetails(userProfileUpdateRequest, userDetails);

        return ResponseEntity.ok(response);
    }
}
