package az.qala.permissionbased.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ApiError {
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
