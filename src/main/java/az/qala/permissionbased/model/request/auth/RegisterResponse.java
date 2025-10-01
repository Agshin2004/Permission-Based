package az.qala.permissionbased.model.request.auth;

import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.validator.annot.ValidRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse implements Serializable {
    @Size(max = 32)
    @NotBlank(message = "username cannot be empty")
    private String username;

    @Email
    @NotBlank(message = "Email must be provided")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "password cannot be empty")
    @Size(max = 50)
    private String password;

    @ValidRoles
    private List<UserRoles> roles;
}
