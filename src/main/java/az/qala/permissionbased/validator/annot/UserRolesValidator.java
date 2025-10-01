package az.qala.permissionbased.validator.annot;

import az.qala.permissionbased.model.enums.UserRoles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;

public class UserRolesValidator implements ConstraintValidator<ValidRoles, List<UserRoles>> {

    @Override
    public boolean isValid(List<UserRoles> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return true; // true is valid
        }

        boolean allValid = roles.stream().allMatch(Objects::nonNull); // == role -> role != null

        if (!allValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Roles cannot be null"
            ).addConstraintViolation();
        }

        return allValid;

    }
}
