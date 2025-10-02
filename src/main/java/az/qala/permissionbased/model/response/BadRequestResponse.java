package az.qala.permissionbased.model.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class BadRequestResponse {
    private final String message;
    private final String path;
    private LocalDateTime timestamp = LocalDateTime.now();
}
