package rs.meningsistem.stamparija.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String normalized = value.replaceAll("[\\s\\-\\/]", "");
        return normalized.matches("[0-9]+") && normalized.length() >= 9 && normalized.length() <= 13;
    }
}
