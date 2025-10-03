package az.qala.permissionbased.model.response.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class LoginResponse implements Serializable {
    private final String jwt;
}
