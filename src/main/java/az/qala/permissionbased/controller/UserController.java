package az.qala.permissionbased.controller;

import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.request.auth.RegisterResponse;
import az.qala.permissionbased.model.response.GenericResponse;
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

    @PostMapping("/users")
    public ResponseEntity<GenericResponse<UserDTO>> registerUser(@Valid @RequestBody RegisterResponse registerResponse) {
        GenericResponse<UserDTO> response = userService.createUser(registerResponse);
        return ResponseEntity.ok(response);
    }
}
