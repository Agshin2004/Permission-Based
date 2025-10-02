package az.qala.permissionbased.model.dto;

import az.qala.permissionbased.model.enums.RegistrationStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO implements Serializable {
    public UUID id;

    public String username;
    public String email;

    public LocalDateTime createdAt;
    public LocalDateTime lastLogin;

    public RegistrationStatus registrationStatus;

    public String jwt;

    public List<RoleDTO> roles;

}
