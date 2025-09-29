package az.qala.permissionbased.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN"),
    LAW_DEPT("LAW_DEPARTMENT"),
    UNDERWRITING("UNDERWRITING");

    private final String role;

    public static UserRoles fromName(String role) {
        return UserRoles.valueOf(role.toUpperCase());
    }
}
