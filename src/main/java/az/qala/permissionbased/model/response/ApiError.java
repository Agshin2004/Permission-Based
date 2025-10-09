package az.qala.permissionbased.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ApiError {
    private String message;
    private String path;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(String message, String path) {
        this.message = message;
        this.path = path;
    }

    public ApiError(String message, String path, LocalDateTime timestamp) {
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

}
