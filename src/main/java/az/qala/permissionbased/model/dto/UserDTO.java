package az.qala.permissionbased.model.dto;

import az.qala.permissionbased.model.enums.RegistrationStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDTO implements Serializable {
    public UUID id;

    public String username;
    public String email;

    public LocalDateTime created;
    public LocalDateTime lastLogin;

    public RegistrationStatus registrationStatus;

    public List<RoleDTO> roles;

}
