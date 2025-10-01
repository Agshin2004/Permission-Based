package az.qala.permissionbased.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    EMAIL_ALREADY_EXISTS("Email %s already exists"),
    USERNAME_ALREADY_EXISTS("Username %s already exists"),
    ROLE_NOT_FOUND("Role %s not found");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
