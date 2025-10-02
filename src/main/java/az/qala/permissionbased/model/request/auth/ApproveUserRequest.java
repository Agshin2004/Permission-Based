package az.qala.permissionbased.model.request.auth;

import az.qala.permissionbased.model.enums.RegistrationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveUserRequest implements Serializable {
    @NotNull
    private UUID userId;

    @NotNull
    private RegistrationStatus registrationStatus;
}
