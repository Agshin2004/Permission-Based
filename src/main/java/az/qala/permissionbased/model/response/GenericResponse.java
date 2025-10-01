package az.qala.permissionbased.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GenericResponse<P> implements Serializable {
    private String message;
    private P body;
    private Integer statusCode;
    private LocalDateTime timestamp;

    private GenericResponse() {
    }

    public static <T> GenericResponse<T> success(
            String message,
            T body,
            Integer statusCode
    ) {
        return new GenericResponse<>(message, body, statusCode, LocalDateTime.now());
    }
}
