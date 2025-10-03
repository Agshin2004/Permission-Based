package az.qala.permissionbased.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    @Size(max = 32)
    @NotBlank(message = "Email must be provided")
    @Email
    private String email;

    @NotBlank(message = "password must be provided")
    @Size(max = 50)
    private String password;

}
