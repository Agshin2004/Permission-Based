package az.qala.permissionbased.advice;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.response.ApiError;
import az.qala.permissionbased.model.response.BadRequestResponse;
import az.qala.permissionbased.model.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(DataExistsException.class)
    public ResponseEntity<String> handleDataExists(DataExistsException ex) {
        log.info("Exception caught in handleNotFound", ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiError> handleDataNotFound(DataNotFoundException ex, HttpServletRequest request) {
        var response = new ApiError(ex.getMessage(), request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
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

    // Handles JSON parse errors (like invalid enum value, invalid UUID, etc.)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BadRequestResponse> handleInvalidFormat(HttpMessageNotReadableException ex, HttpServletRequest request) {

        var response = new BadRequestResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
