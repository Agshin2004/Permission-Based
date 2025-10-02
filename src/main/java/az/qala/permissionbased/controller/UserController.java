package az.qala.permissionbased.controller;

import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.request.auth.ApproveUserRequest;
import az.qala.permissionbased.model.request.auth.LoginRequest;
import az.qala.permissionbased.model.request.auth.RegisterRequest;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.auth.LoginResponse;
import az.qala.permissionbased.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
}
