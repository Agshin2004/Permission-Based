package az.qala.permissionbased.validator.annot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
// know which class runs the validation
// Calls the isValid() method in UserRolesValidator
// If invalid, throws a structured validation error using the message() text.
@Constraint(validatedBy = UserRolesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {
    String message() default "Invalid roles provided"; // Default error message if the validation fails

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
