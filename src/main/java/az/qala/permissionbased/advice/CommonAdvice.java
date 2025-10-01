package az.qala.permissionbased.advice;

import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.model.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(DataExistsException.class)
    @ResponseBody
    public ResponseEntity<String> handleNotFound(DataExistsException ex) {
        log.info("Exception caught in handleNotFound", ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        // getBindingResult() returns a BindingResult object, which contains all the details about what went wrong during validation
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.ok(GenericResponse.success("failed", errors, 400));
    }
}
