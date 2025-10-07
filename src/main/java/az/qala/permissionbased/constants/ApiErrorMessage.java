package az.qala.permissionbased.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    EMAIL_ALREADY_EXISTS("Email %s already exists"),
    USERNAME_ALREADY_EXISTS("Username %s already exists"),
    POST_TAG_ALREADY_EXISTS("Post tag with id %d %d already exists"),

    ROLE_NOT_FOUND("Role %s not found"),
    USER_NOT_FOUND("User %s not found"),
    POST_NOT_FOUND("Post with id %d not found"),
    TAG_NOT_FOUND("Tag with id %d not found"),
    WRONG_CREDENTIALS("User not found"),
    NOT_FOUND("not found");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
