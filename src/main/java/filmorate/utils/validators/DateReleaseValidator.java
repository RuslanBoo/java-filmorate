package filmorate.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

class DateReleaseValidator implements ConstraintValidator<ReleaseDate, Temporal> {
    public static final LocalDate RELEASE_START = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        return value == null || LocalDate.from(value).isAfter(RELEASE_START);
    }
}